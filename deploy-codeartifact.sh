#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'USAGE'
Deploy this Maven project to AWS CodeArtifact.

Prerequisites:
  - AWS CLI is installed and authenticated with an IAM user/role, not root.
  - The IAM principal can call:
      codeartifact:GetAuthorizationToken
      codeartifact:GetRepositoryEndpoint
      codeartifact:ReadFromRepository
      codeartifact:PublishPackageVersion
      codeartifact:PutPackageMetadata
      sts:GetServiceBearerToken
  - ~/.m2/settings.xml has matching server ids, for example:
      codeartifact
      ipss-maven-release
      ipss-maven-snapshot

Usage:
  ./deploy-codeartifact.sh [options] [-- extra-maven-args]

Options:
  --profile NAME        AWS profile to use
  --region NAME         AWS region, default: us-east-1
  --domain NAME         CodeArtifact domain, default: ipss
  --domain-owner ID     AWS account/domain owner, default: 078183419290
  --repository NAME     Repository for endpoint check, default: maven-release
  --skip-tests          Add -DskipTests to Maven deploy
  --dry-run             Print identity and endpoint checks, but do not deploy
  -h, --help            Show this help

Examples:
  ./deploy-codeartifact.sh --profile ipss-publish --skip-tests
  ./deploy-codeartifact.sh --skip-tests -- -Dgpg.skip=true
USAGE
}

AWS_PROFILE_NAME="${AWS_PROFILE:-}"
AWS_REGION="us-east-1"
CODEARTIFACT_DOMAIN="ipss"
CODEARTIFACT_DOMAIN_OWNER="078183419290"
CODEARTIFACT_REPOSITORY="maven-release"
SKIP_TESTS=false
DRY_RUN=false
EXTRA_MAVEN_ARGS=()

while [[ $# -gt 0 ]]; do
  case "$1" in
    --profile)
      shift
      [[ $# -gt 0 ]] || { echo "Missing value for --profile" >&2; exit 2; }
      AWS_PROFILE_NAME="$1"
      ;;
    --region)
      shift
      [[ $# -gt 0 ]] || { echo "Missing value for --region" >&2; exit 2; }
      AWS_REGION="$1"
      ;;
    --domain)
      shift
      [[ $# -gt 0 ]] || { echo "Missing value for --domain" >&2; exit 2; }
      CODEARTIFACT_DOMAIN="$1"
      ;;
    --domain-owner)
      shift
      [[ $# -gt 0 ]] || { echo "Missing value for --domain-owner" >&2; exit 2; }
      CODEARTIFACT_DOMAIN_OWNER="$1"
      ;;
    --repository)
      shift
      [[ $# -gt 0 ]] || { echo "Missing value for --repository" >&2; exit 2; }
      CODEARTIFACT_REPOSITORY="$1"
      ;;
    --skip-tests)
      SKIP_TESTS=true
      ;;
    --dry-run)
      DRY_RUN=true
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    --)
      shift
      EXTRA_MAVEN_ARGS=("$@")
      break
      ;;
    *)
      echo "Unknown argument: $1" >&2
      usage
      exit 2
      ;;
  esac
  shift
done

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${script_dir}"

aws_args=(--region "${AWS_REGION}")
if [[ -n "${AWS_PROFILE_NAME}" ]]; then
  aws_args+=(--profile "${AWS_PROFILE_NAME}")
  export AWS_PROFILE="${AWS_PROFILE_NAME}"
fi

require_command() {
  command -v "$1" >/dev/null 2>&1 || {
    echo "Required command not found: $1" >&2
    exit 1
  }
}

require_command aws
require_command mvn

identity_arn="$(aws "${aws_args[@]}" sts get-caller-identity --query Arn --output text)"
echo "AWS identity: ${identity_arn}"
if [[ "${identity_arn}" == *":root" ]]; then
  echo "Refusing to deploy with AWS root credentials. Use an IAM user or assumed role." >&2
  exit 1
fi

endpoint="$(aws "${aws_args[@]}" codeartifact get-repository-endpoint \
  --domain "${CODEARTIFACT_DOMAIN}" \
  --domain-owner "${CODEARTIFACT_DOMAIN_OWNER}" \
  --repository "${CODEARTIFACT_REPOSITORY}" \
  --format maven \
  --query repositoryEndpoint \
  --output text)"
echo "CodeArtifact endpoint: ${endpoint}"

token="$(aws "${aws_args[@]}" codeartifact get-authorization-token \
  --domain "${CODEARTIFACT_DOMAIN}" \
  --domain-owner "${CODEARTIFACT_DOMAIN_OWNER}" \
  --query authorizationToken \
  --output text)"

if [[ -z "${token}" || "${token}" == "None" ]]; then
  echo "Could not obtain CODEARTIFACT_AUTH_TOKEN." >&2
  exit 1
fi
echo "CODEARTIFACT_AUTH_TOKEN acquired."

mvn_args=(deploy)
if ${SKIP_TESTS}; then
  mvn_args+=(-DskipTests)
fi
if [[ ${#EXTRA_MAVEN_ARGS[@]} -gt 0 ]]; then
  mvn_args+=("${EXTRA_MAVEN_ARGS[@]}")
fi

echo "Maven command: mvn ${mvn_args[*]}"
if ${DRY_RUN}; then
  echo "Dry run requested; skipping Maven deploy."
  exit 0
fi

CODEARTIFACT_AUTH_TOKEN="${token}" mvn "${mvn_args[@]}"
