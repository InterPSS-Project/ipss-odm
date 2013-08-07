#!/usr/bin/perl -w
# ODM2PSAT converts ODM data file into PSAT data file
#
# ODM2PSAT <OPTIONS> FILEINPUT <FILEOUTPUT>
#
# Author:  Federico Milano
# Date:    29-Feb-2008
# Version: 1.0.0
#
# E-mail:  Federico.Milano@uclm.es
#

use strict;
use XML::Simple;
use Data::Dumper;

# -----------------------------------------------------------------------
# variable declaration
# -----------------------------------------------------------------------
my $xml = new XML::Simple;

my $nargin = 0;
my $verbose = 0;
my $helpmsg = 0;
my ($i,$h,$j);

my $nbus = -1;
my $nsw = -1;
my $npv = -1;
my $npq = -1;
my $npqgen = -1;
my $nsh = -1;
my $narea = -1;
my $nzone = -1;
my $nline = -1;

my $pbas = 100;
my $fbas = 60;
my ($data,$temp,$gen,$load,$shunt,$area,%nvpair);
my $apptype = "Transmission";
my $base;
my $format;
my $type = "";
my $pos = 0;

my ($title1,$title2,$title3,$title4);
my $year = "n.d.";
my $originator = "unknown";
my $season = "";
my $date = "unknown";
my $id = "Generic ODM case";

my (%busid,%lineid);

my (@buskv,@busname,@busvol,@area,@zone,@busang,@vmax,@vmin);
my (@pgen,@qgen,@ppqgen,@qpqgen,@psw,@qsw,@qswmax,@qswmin,@qmin,@qmax);
my (@swidx,@pvidx,@pqidx,@pqgenidx,@pload,@qload);
my (@shidx,@shsb,@shvb,@pcap,@qcap);
my (@busfr,@busto,@rline,@xline,@bshunt,@gshunt,@tap,@phs,@linevb,@linesb,@kt,@imax);
my (@areanum,@areaslack,@areaexp,@areatol,@areaname);
my (@zonenum,@zonename);

# -----------------------------------------------------------------------
# check inputs
# -----------------------------------------------------------------------
$nargin = @ARGV;
$nargin || die "Error: No input data file.\n";

# -----------------------------------------------------------------------
# check options
# -----------------------------------------------------------------------
while ($ARGV[0] =~ /^-/) {
   if ($ARGV[0] =~ /v/) {$verbose = 1;}
   if ($ARGV[0] =~ /h/) {$helpmsg = 1;}
   shift(@ARGV);
   $nargin--;
   if ($nargin == 0) {
       last;
   }
}
$title1 = 'Generic ODM data format '.$ARGV[0];

# -----------------------------------------------------------------------
# help (if requested)
# -----------------------------------------------------------------------
if ($helpmsg) {
   print "\nODM2PSAT converts ODM data files into PSAT data files.\n\n";
   print "ipss2psat <options> fileinput <fileoutput>\n";
   print "  -v   verbose\n";
   print "  -h   print this help and exit\n\n";
   print "Author:   Federico Milano\n";
   print "Date:     29-Feb-2008\n";
   print "Version:  1.0.0\n\n";
   print "E-mail:   Federico.Milano\@uclm.es\n";
   die "\n";
}

# -----------------------------------------------------------------------
# define output file name (if necessary)
# -----------------------------------------------------------------------
if ($nargin == 1) {
   $ARGV[1] = $ARGV[0];
   $ARGV[1] =~ s/^d*_*/d_/;
   $ARGV[1] =~ s/[^\w\.]/_/g;
   $ARGV[1] =~ s/\..+$/.m/;
} elsif ($nargin == 0) {
   die "Error: Input file name is missing.\n";
}

# -----------------------------------------------------------------------
# parse input data file
# -----------------------------------------------------------------------
print "Parsing ODM data file \"$ARGV[0]\"...\n";

# Read data file and parse XML input into a Perl structure
$data = $xml->XMLin($ARGV[0], keyattr => [], forcearray => [ qw (pss:interchange pss:lossZone) ]);

## uncomment to write down the perl data structure
#open OUT, ">dumper.txt" || die "cannot open dumper.txt: $!\n";
#print OUT Dumper($data);
#close(OUT);

print "ODM data version: " . $data->{"pss:schemaVersion"} . "\n";
$apptype = $data->{"pss:networkCategory"};
print "Network category: " . $apptype . "\n";
print "Analysis category: " . $data->{"pss:analysisCategory"} . "\n";

if (defined($data->{"pss:baseCase"}->{"pss:baseKva"})) {
   if ($data->{"pss:baseCase"}->{"pss:baseKvaUnit"} eq "MVA") {
       $pbas = $data->{"pss:baseCase"}->{"pss:baseKva"};
   } elsif ($data->{"pss:baseCase"}->{"pss:baseKvaUnit"} eq "KVA") {
       $pbas = $data->{"pss:baseCase"}->{"pss:baseKva"}/1000;
   }
}

if (defined($data->{"pss:baseCase"}->{"pss:basePower"})) {
   $temp = $data->{"pss:baseCase"}->{"pss:basePower"};
   if ($temp->{"pss:unit"} eq "MVA") {
       $pbas = $temp->{"pss:power"};
   } elsif ($temp->{"pss:unit"} eq "KVA") {
       $pbas = $temp->{"pss:power"}/1000;
   }
}

# -----------------------------------------------------------------------
# bus data
# -----------------------------------------------------------------------

for $temp ( @{$data->{"pss:baseCase"}->{"pss:busList"}->{"pss:bus"}} ) {

   $nbus++;
   $busid{$temp->{"pss:id"}} = $nbus;

   # default values
   $busname[$nbus] = "Bus " . $temp->{"pss:id"};
   $buskv[$nbus] = 1;
   $busvol[$nbus] = 1;
   $busang[$nbus] = 0;
   $vmax[$nbus] = 1.1;
   $vmin[$nbus] = 0.9;
   $area[$nbus] = 1;
   $zone[$nbus] = 1;

   # bus name
   if (defined($temp->{"pss:name"})) {
       $busname[$nbus] = $temp->{"pss:name"};
   }

   # voltage rate (kV)
   if (defined($temp->{"pss:baseVoltage"})) {
       if (defined($temp->{"pss:baseVoltageUnit"})) {
           if ($temp->{"pss:baseVoltageUnit"} eq "KV") {
               $buskv[$nbus] = $temp->{"pss:baseVoltage"};
           } elsif ($temp->{"pss:baseVoltageUnit"} eq "VOLT") {
               $buskv[$nbus] = $temp->{"pss:baseVoltage"}/1000;
           }
       } elsif (defined($temp->{"pss:baseVoltage"}->{"pss:unit"})) {
           if ($temp->{"pss:baseVoltage"}->{"pss:unit"} eq "KV") {
               $buskv[$nbus] = $temp->{"pss:baseVoltage"}->{"pss:voltage"};
           } elsif ($temp->{"pss:baseVoltage"}->{"pss:unit"} eq "VOLT") {
               $buskv[$nbus] = $temp->{"pss:baseVoltage"}->{"pss:voltage"}/1000;
           }
       }
   }

   # area number
   if (defined($temp->{"pss:area"})) {
       $area[$nbus] = $temp->{"pss:area"};
   }

   # zone number
   if (defined($temp->{"pss:zone"})) {
       $zone[$nbus] = $temp->{"pss:zone"};
   }

   # voltage magnitude (p.u.)
   if (defined($temp->{"pss:loadflowBusData"}->{"pss:voltage"})) {
       $h = $temp->{"pss:loadflowBusData"}->{"pss:voltage"};
       if ($h->{"pss:unit"} eq "PU") {
           $busvol[$nbus] = $h->{"pss:voltage"};
       } elsif ($h->{"pss:unit"} eq "KV") {
           $busvol[$nbus] = $h->{"pss:voltage"}/$buskv[$nbus];
       } elsif ($h->{"pss:unit"} eq "VOLT") {
           $busvol[$nbus] = $h->{"pss:voltage"}/$buskv[$nbus]/1000;
       }
   }

   # voltage angle (rad)
   if (defined($temp->{"pss:loadflowBusData"}->{"pss:angle"})) {
       $h = $temp->{"pss:loadflowBusData"}->{"pss:angle"};
       if ($h->{"pss:unit"} eq "DEG") {
           $busang[$nbus] = 0.017453292519943*$h->{"pss:angle"};
       } elsif ($h->{"pss:unit"} eq "RAD") {
           $busang[$nbus] = $h->{"pss:angle"};
       }
   }

   # PV and slack data
   if (defined($temp->{"pss:loadflowBusData"}->{"pss:genData"})) {
       $gen = $temp->{"pss:loadflowBusData"}->{"pss:genData"};
       if ($gen->{"pss:code"} eq "PV") {
           $pvidx[++$npv] = $nbus;
           if ($gen->{"pss:gen"}->{"pss:unit"} eq "MVA") {
               $pgen[$npv] = $gen->{"pss:gen"}->{"pss:p"}/$pbas;
               $qgen[$npv] = $gen->{"pss:gen"}->{"pss:q"}/$pbas;
           } elsif ($gen->{"pss:gen"}->{"pss:unit"} eq "PU") {
               $pgen[$npv] = $gen->{"pss:gen"}->{"pss:p"};
               $qgen[$npv] = $gen->{"pss:gen"}->{"pss:q"};
           }
           if (defined($gen->{"pss:qGenLimit"})) {
               $h = $gen->{"pss:qGenLimit"};
               if ($h->{"pss:qLimitUnit"} eq "MVAR") {
                   $qmax[$npv] = $h->{"pss:qLimit"}->{"pss:max"}/$pbas;
                   $qmin[$npv] = $h->{"pss:qLimit"}->{"pss:min"}/$pbas;
               } elsif ($h->{"pss:qLimitUnit"} eq "PU") {
                   $qmax[$npv] = $h->{"pss:qLimit"}->{"pss:max"};
                   $qmin[$npv] = $h->{"pss:qLimit"}->{"pss:min"};
               }
           } else {
               $qmax[$npv] = 0;
               $qmin[$npv] = 0;
           }
       } elsif ($gen->{"pss:code"} eq "PQ") {
           $pqgenidx[++$npqgen] = $nbus;
           if ($gen->{"pss:gen"}->{"pss:unit"} eq "MVA") {
               $ppqgen[$npqgen] = $gen->{"pss:gen"}->{"pss:p"}/$pbas;
               $qpqgen[$npqgen] = $gen->{"pss:gen"}->{"pss:q"}/$pbas;
           } elsif ($gen->{"pss:gen"}->{"pss:unit"} eq "PU") {
               $ppqgen[$npqgen] = $gen->{"pss:gen"}->{"pss:p"};
               $qpqgen[$npqgen] = $gen->{"pss:gen"}->{"pss:q"};
           }
       } elsif ($gen->{"pss:code"} eq "SWING") {
           $swidx[++$nsw] = $nbus;
           if (defined($gen->{"pss:gen"})) {
               if ($gen->{"pss:gen"}->{"pss:unit"} eq "MVA") {
                   $psw[$nsw] = $gen->{"pss:gen"}->{"pss:p"}/$pbas;
                   $qsw[$nsw] = $gen->{"pss:gen"}->{"pss:q"}/$pbas;
               } elsif ($gen->{"pss:gen"}->{"pss:unit"} eq "PU") {
                   $psw[$nsw] = $gen->{"pss:gen"}->{"pss:p"};
                   $qsw[$nsw] = $gen->{"pss:gen"}->{"pss:q"};
               }
           } else {
               $psw[$nsw] = 0;
               $qsw[$nsw] = 0;
           }
           if (defined($gen->{"pss:gGenLimit"})) {
               if ($gen->{"pss:qGenLimit"}->{"pss:qLimitUnit"} eq "MVAR") {
                   $qswmax[$nsw] = $gen->{"pss:qGenLimit"}->{"pss:qLimit"}->{"pss:max"}/$pbas;
                   $qswmin[$nsw] = $gen->{"pss:qGenLimit"}->{"pss:qLimit"}->{"pss:min"}/$pbas;
               } elsif ($gen->{"pss:qGenLimit"}->{"pss:qLimitUnit"} eq "PU") {
                   $qswmax[$nsw] = $gen->{"pss:qGenLimit"}->{"pss:qLimit"}->{"pss:max"};
                   $qswmin[$nsw] = $gen->{"pss:qGenLimit"}->{"pss:qLimit"}->{"pss:min"};
               }
           } else {
               $qswmax[$nsw] = 0;
               $qswmin[$nsw] = 0;
           }
       }
   }

   # load data
   if (defined($temp->{"pss:loadflowBusData"}->{"pss:loadData"})) {
       $load = $temp->{"pss:loadflowBusData"}->{"pss:loadData"};
       if ($load->{"pss:code"} eq "CONST_P") {
           $pqidx[++$npq] = $nbus;
           if ($load->{"pss:load"}->{"pss:unit"} eq "MVA") {
               $pload[$npq] = $load->{"pss:load"}->{"pss:p"}/$pbas;
               $qload[$npq] = $load->{"pss:load"}->{"pss:q"}/$pbas;
           } elsif ($load->{"pss:load"}->{"pss:unit"} eq "PU") {
               $pload[$npq] = $load->{"pss:load"}->{"pss:p"};
               $qload[$npq] = $load->{"pss:load"}->{"pss:q"};
           }
       }
   }

   # shunt admittance data
   if (defined($temp->{"pss:loadflowBusData"}->{"pss:shuntY"})) {
       $shunt = $temp->{"pss:loadflowBusData"}->{"pss:shuntY"};
       addshunt($shunt,$nbus,$pbas,$buskv[$nbus]);
   }
}

# -----------------------------------------------------------------------
# line data
# -----------------------------------------------------------------------

for $temp ( @{$data->{"pss:baseCase"}->{"pss:branchList"}->{"pss:branch"}} ) {

   $nline++;
   $busfr[$nline] = $busid{$temp->{"pss:fromBus"}->{"pss:idRef"}};
   $busto[$nline] = $busid{$temp->{"pss:toBus"}->{"pss:idRef"}};
   if (defined($temp->{"pss:id"})) {
       $lineid{$temp->{"pss:id"}} = $nline;
   } else {
       $lineid{$busfr[$nline] . $busto[$nline] . $temp->{"pss:circuitId"}} = $nline;
   }
   # default values
   $linesb[$nline] = $pbas;
   $linevb[$nline] = $buskv[$busfr[$nline]];
   $rline[$nline] = 0;
   $xline[$nline] = 0.001;
   $bshunt[$nline] = 0;
   $gshunt[$nline] = 0;
   $tap[$nline] = 0;
   $phs[$nline] = 0;
   $kt[$nline] = 0;
   $imax[$nline] = 0;

   if ($temp->{"pss:loadflowBranchData"}->{"pss:code"} eq "Line") {

       $h = $temp->{"pss:loadflowBranchData"}->{"pss:lineData"};
       ratings($h);
       impedance($h);
       lineshunt($h);

   } elsif ($temp->{"pss:loadflowBranchData"}->{"pss:code"} eq "Transformer") {

       $kt[$nline] = $linevb[$nline]/$buskv[$busto[$nline]];
       $h = $temp->{"pss:loadflowBranchData"}->{"pss:xformerData"};
       ratings($h);
       impedance($h);
       lineshunt($h);
       tapratio($h);

   } elsif ($temp->{"pss:loadflowBranchData"}->{"pss:code"} eq "PhaseShiftXformer") {

       $kt[$nline] = $linevb[$nline]/$buskv[$busto[$nline]];
       $h = $temp->{"pss:loadflowBranchData"}->{"pss:phaseShiftXfrData"};
       ratings($h);
       impedance($h);
       tapratio($h);
       lineshunt($h);
       phaseshifting($h);

   } else {

       # other series component

   }

   # rating limits
   if (defined($temp->{"pss:loadflowBranchData"}->{"pss:ratingLimit"})) {
       $h = $temp->{"pss:loadflowBranchData"}->{"pss:ratingLimit"};
       if (defined($h->{"pss:currentRating"})) {
           if ($h->{"pss:currentRatingUnit"} eq "KA") {
               $base = $linesb[$nline]/$linevb[$nline]/1.732050807568877;
               $imax[$nline] = $h->{"pss:currentRating"}/$base;
           } elsif ($h->{"pss:currentRatingUnit"} eq "AMP") {
               $base = $linesb[$nline]/$linevb[$nline]/0.001732050807569;
               $imax[$nline] = $h->{"pss:currentRating"}/$base;
           } elsif ($h->{"pss:currentRatingUnit"} eq "PU") {
               $imax[$nline] = $h->{"pss:currentRating"};
           }
       }
   }

}

# -----------------------------------------------------------------------
# area data
# -----------------------------------------------------------------------

for $temp ( @{$data->{"pss:baseCase"}->{"pss:interchangeList"}->{"pss:interchange"}} ) {

   unless (defined($temp->{"pss:ieeeCDFInterchange"})) { next };
   $area = $temp->{"pss:ieeeCDFInterchange"};
   $narea++;
   $areaname[$narea] = $area->{"pss:areaName"};
   $areanum[$narea] = $area->{"pss:areaNumber"};

   # default values
   $areaslack[$narea] = $swidx[0]+1;
   $areaexp[$narea] = 0;
   $areatol[$narea] = 0;

   if (defined($area->{"pss:swingBus"})) {
       $areaslack[$narea] = $busid{$area->{"pss:swingBus"}->{"pss:idRef"}}+1;
   }
   if (defined($area->{"pss:interchangePowerUnit"})) {
       if ($area->{"pss:interchangePowerUnit"} eq "MW") {
           $areaexp[$narea] = $area->{"pss:interchangePower"}/$pbas;
           $areatol[$narea] = $area->{"pss:interchangeErrTolerance"}/$pbas;
       } elsif ($area->{"pss:interchangePowerUnit"} eq "PU") {
           $areaexp[$narea] = $area->{"pss:interchangePower"};
           $areatol[$narea] = $area->{"pss:interchangeErrTolerance"};
       }
   } elsif (defined($area->{"pss:interchangePower"}->{"pss:unit"})) {
       if ($area->{"pss:interchangePower"}->{"pss:unit"} eq "MW") {
           $areaexp[$narea] = $area->{"pss:interchangePower"}->{"pss:p"}/$pbas;
           $areatol[$narea] = $area->{"pss:interchangeErrTolerance"}/$pbas;
       } elsif ($area->{"pss:interchangePower"}->{"pss:unit"} eq "PU") {
           $areaexp[$narea] = $area->{"pss:interchangePower"}->{"pss:p"};
           $areatol[$narea] = $area->{"pss:interchangeErrTolerance"};
       }
   }

}

# -----------------------------------------------------------------------
# zone data
# -----------------------------------------------------------------------

for $temp ( @{$data->{"pss:baseCase"}->{"pss:lossZoneList"}->{"pss:lossZone"}} ) {
   $nzone++;
   $zonename[$nzone] = $temp->{"pss:zoneName"};
   $zonenum[$nzone] = $temp->{"pss:zoneNumber"};
}

# -----------------------------------------------------------------------
# case information
# -----------------------------------------------------------------------

$title1 = $data->{"pss:baseCase"}->{"pss:id"};
$title1 =~ s/_/ /g;
$title2 = "Case: " . ($nbus+1) . "-bus " . ($nline+1) . "-line system.";
$title3 = "Date: " . $date;
$title4 = "Originator: " . $originator;

if (defined($data->{"pss:baseCase"}->{"pss:nvPairList"})) {
   for $temp ( @{$data->{"pss:baseCase"}->{"pss:nvPairList"}->{"pss:nvPair"}} ) {
       $nvpair{$temp->{"pss:name"}} = $temp->{"pss:value"};
   }

   if (defined($nvpair{Season})) { $season = $nvpair{Season}; }
   if (defined($nvpair{Year})) { $year = $nvpair{Year}; }
   if (defined($nvpair{Date})) { $date = $nvpair{Date}; }
   if (defined($nvpair{"Originator Name"})) { $originator = $nvpair{"Originator Name"}; }
   if (defined($nvpair{"Case Identification"})) { $id = $nvpair{"Case Identification"}; }

   $title2 = "Case:" . $id . ", " . $year . " " . $season .
       " (" .  ($nbus+1) . "-bus " . ($nline+1) . "-line system)";
   $title3 = "Date: " . $date;
   $title4 = "Originator: " . $originator;

}

# -----------------------------------------------------------------------
# open output data file
# -----------------------------------------------------------------------
print "Writing PSAT file \"$ARGV[1]\"...\n";
open(OUT,">$ARGV[1]") || die "cannot open $ARGV[1]: $!\n";

# -----------------------------------------------------------------------
# write output data file
# -----------------------------------------------------------------------
print OUT "% File generated by PSAT from ODM data file. \n";
print OUT "% "."-" x 78 . "\n";
print OUT "% Author:   Federico Milano\n";
print OUT "% E-mail:   Federico.Milano\@uclm.es \n";
print OUT "% "."-" x 78 . "\n";
print OUT "% $title1 \n";
print OUT "% $title2 \n";
unless ($date eq "unknown") { print OUT "% $title3 \n"; }
unless ($originator eq "unknown") { print OUT "% $title4 \n"; }
print OUT "\n";

# -----------------------------------------------------------------------
# write Bus.con
# -----------------------------------------------------------------------
if ($nbus >= 0) {
   $format = "%4d %7.2f %8.5f  %8.5f %2d %2d;\n";
   print OUT "Bus.con = [ ...\n";
   for ($i = 0; $i <= $nbus; $i++) {
       printf OUT $format,$i+1,$buskv[$i],$busvol[$i],
       $busang[$i],$area[$i],$zone[$i];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write SW.con
# -----------------------------------------------------------------------
if ($nsw >= 0) {
   print OUT "SW.con = [ ...\n";
   $format = "%4d $pbas %7.2f " . "%8.5f " x 7 . " 1 1 1;\n";
   for ($i = 0; $i <= $nsw; $i++) {
       $h = $swidx[$i];
       printf OUT $format,$h+1,$buskv[$h],$busvol[$h],$busang[$h],
       $qswmax[$i],$qswmin[$i],$vmax[$h],$vmin[$h],$psw[$i];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write PV.con
# -----------------------------------------------------------------------
if ($npv >= 0) {
   print OUT "PV.con = [ ...\n";
   $format = "%4d $pbas %7.2f " . "%8.5f " x 6 . " 1 1;\n";
   for ($i = 0; $i <= $npv; $i++) {
       $h = $pvidx[$i];
       printf OUT $format,$h+1,$buskv[$h],$pgen[$i],
       $busvol[$h],$qmax[$i],$qmin[$i],$vmax[$h],$vmin[$h];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write PQgen.con
# -----------------------------------------------------------------------
if ($npqgen >= 0) {
   print OUT "PQgen.con = [ ...\n";
   $format = "%4d $pbas %7.2f " . "%8.5f " x 4 . " 1 1;\n";
   for ($i = 0; $i <= $npqgen; $i++) {
       $h = $pqgenidx[$i];
       printf OUT $format,$h+1,$buskv[$h],$ppqgen[$i],
       $qpqgen[$i],$vmax[$h],$vmin[$h];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write PQ.con
# -----------------------------------------------------------------------
if ($npq >= 0) {
   print OUT "PQ.con = [ ...\n";
   $format = "%4d $pbas %7.2f " . "%8.5f " x 4 . " 1 1;\n";
   for ($i = 0; $i <= $npq; $i++) {
       $h = $pqidx[$i];
       printf OUT $format,$h+1,$buskv[$h],$pload[$i],
       $qload[$i],$vmax[$h],$vmin[$h];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write Shunt.con
# -----------------------------------------------------------------------
if ($nsh >= 0) {
   print OUT "Shunt.con = [ ...\n";
   $format = "%4d %7.2f %7.2f $fbas %8.5f %8.5f 1;\n";
   for ($i = 0; $i <= $nsh; $i++) {
       $h = $shidx[$i];
       printf OUT $format,$h+1,$shsb[$i],$shvb[$i],$pcap[$i],$qcap[$i];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write Line.con
# -----------------------------------------------------------------------
if ($nline >= 0) {
   print OUT "Line.con = [ ...\n";
   $format = "%4d %4d %7.2f %7.2f $fbas 0 " . "%8.5f " x 7 . " 0 0 1;\n";
   for ($i = 0; $i <= $nline; $i++) {
       printf OUT $format,$busfr[$i]+1,$busto[$i]+1,$linesb[$i],
       $linevb[$i],$kt[$i],$rline[$i],$xline[$i],$bshunt[$i],
       $tap[$i],$phs[$i],$imax[$i];
   }
   printf OUT "   ];\n\n";
}

# -----------------------------------------------------------------------
# write Areas.con
# -----------------------------------------------------------------------
if ($narea >= 0) {
   print OUT "Areas.con = [ ...\n";
   $format = "%4d %4d $pbas %8.5f %8.5f  0;\n";
   for ($i = 0; $i <= $narea; $i++) {
       printf OUT $format, $areanum[$i],$areaslack[$i],
       $areaexp[$i],$areatol[$i];
   }
   printf OUT "  ];\n\n";
}

# -----------------------------------------------------------------------
# write Regions.con
# -----------------------------------------------------------------------
if ($nzone >= 0) {
   print OUT "Regions.con = [ ...\n";
   $format = "%4d 0 $pbas 0 0 0;\n";
   for ($i = 0; $i <= $nzone; $i++) {
       printf OUT $format, $zonenum[$i];
   }
   printf OUT "  ];\n\n";
}

# -----------------------------------------------------------------------
# write bus names
# -----------------------------------------------------------------------
$nbus >= 0 && print OUT "Bus.names = { ...\n";
$h = ($nbus+1) % 5;
if ($h == 0) {$h = 5;}
if (($nbus+1) > 5) {
   for ($i = 0; $i <= $nbus-$h; $i+=5) {
       print OUT "  '$busname[$i]'; '$busname[$i+1]'; " .
           "'$busname[$i+2]'; '$busname[$i+3]'; '$busname[$i+4]';\n";
   }
}
print OUT "  ";
for ($i = $nbus-$h+1; $i <= $nbus-1; $i++) {
   print OUT "'$busname[$i]'; ";
}
print OUT "'$busname[$nbus]'};\n\n";

# -----------------------------------------------------------------------
# write area names
# -----------------------------------------------------------------------
$nzone >= 0 && print OUT "Areas.names = { ...\n";
for ($i = 0; $i <= $nzone-1; $i++) {
   print OUT "  '$zonename[$i]';\n";
}
$nzone >= 0 && print OUT "  '$zonename[$nzone]'};\n\n";

# -----------------------------------------------------------------------
# write region names
# -----------------------------------------------------------------------
$narea >= 0 && print OUT "Regions.names = { ...\n";
for ($i = 0; $i <= $narea-1; $i++) {
   print OUT "  '$areaname[$i]';\n";
}
$narea >= 0 && print OUT "  '$areaname[$narea]'};\n\n";

# -----------------------------------------------------------------------
# close output data file
# -----------------------------------------------------------------------
close(OUT) || die "cannot close $ARGV[1]: $!\n";
print "Conversion completed.\n";

# -----------------------------------------------------------------------
# function for adding a shunt admittance element
# -----------------------------------------------------------------------
sub addshunt {

   my $h = $_[0];
   $shidx[++$nsh] = $_[1];

   # default values
   $pcap[$nsh] = 0;
   $qcap[$nsh] = 0;

   if ($h->{"pss:unit"} eq "PU") {
       $pcap[$nsh] = $h->{"pss:g"};
       $qcap[$nsh] = $h->{"pss:b"};
   } elsif ($h->{"pss:unit"} eq "MICROMHO") {
       my $base = $_[3]*$_[3]/$_[2];
       $pcap[$nsh] = $base*$h->{"pss:g"}/1000000;
       $qcap[$nsh] = $base*$h->{"pss:b"}/1000000;
   }
   $shsb[$nsh] = $_[2];
   $shvb[$nsh] = $_[3];

}

# -----------------------------------------------------------------------
# function for determining branch ratings
# -----------------------------------------------------------------------
sub ratings {

   my $h = $_[0];
   if (defined($h->{"pss:ratingData"})) {
       my $j = $h->{"pss:ratingData"};
       if (defined($j->{"pss:fromRatedVoltage"})) {
           if ($j->{"pss:fromRatedVoltage"}->{"pss:unit"} eq "KV") {
               $linevb[$nline] = $j->{"pss:fromRatedVoltage"}->{"pss:voltage"};
           } elsif ($j->{"pss:fromRatedVoltage"}->{"pss:unit"} eq "VOLT") {
               $linevb[$nline] = $j->{"pss:fromRatedVoltage"}->{"pss:voltage"}/1000;
           }
       }
       if (defined($j->{"pss:toRatedVoltage"})) {
           if ($j->{"pss:toRatedVoltage"}->{"pss:unit"} eq "KV") {
               $kt[$nline] = $linevb[$nline]/$j->{"pss:toRatedVoltage"}->{"pss:voltage"};
           } elsif ($j->{"pss:toRatedVoltage"}->{"pss:unit"} eq "VOLT") {
               $kt[$nline] = $linevb[$nline]/($j->{"pss:toRatedVoltage"}->{"pss:voltage"}/1000);
           }
       }
       if (defined($j->{"pss:ratedPower"})) {
           if ($j->{"pss:ratedPower"}->{"pss:unit"} eq "MVA") {
               $linesb[$nline] = $j->{"pss:ratedPower"}->{"pss:p"};
           } elsif ($j->{"pss:ratedPower"}->{"pss:unit"} eq "KVA") {
               $linesb[$nline] = $j->{"pss:ratedPower"}->{"pss:p"}/1000;
           }
       }
   }
}

# -----------------------------------------------------------------------
# function for determining branch impedance
# -----------------------------------------------------------------------
sub impedance {

   my $h = $_[0];
   if ($h->{"pss:z"}->{"pss:unit"} eq "PU") {
       $rline[$nline] = $h->{"pss:z"}->{"pss:r"};
       $xline[$nline] = $h->{"pss:z"}->{"pss:x"};
   } elsif ($h->{"pss:z"}->{"pss:unit"} eq "OHM") {
       my $base = $linevb[$nline]*$linevb[$nline]/$linesb[$nline];
       $rline[$nline] = $h->{"pss:z"}->{"pss:r"}/$base;
       $xline[$nline] = $h->{"pss:z"}->{"pss:x"}/$base;
   }
}

# -----------------------------------------------------------------------
# function for determining branch tap ratio
# -----------------------------------------------------------------------
sub tapratio {

   my $h = $_[0];
   if (defined($h->{"pss:fromTurnRatio"})) {
       $tap[$nline] = $h->{"pss:fromTurnRatio"};
   }
   if (defined($h->{"pss:toTurnRatio"})) {
       $tap[$nline] = 1/$h->{"pss:toTurnRatio"};
   }
}

# -----------------------------------------------------------------------
# function for determining branch phase shifting
# -----------------------------------------------------------------------
sub phaseshifting {

   my $h = $_[0];
   if (defined($h->{"pss:fromAngle"})) {
       if ($h->{"pss:fromAngle"}->{"pss:unit"} eq "DEG") {
           $phs[$nline] = $h->{"pss:fromAngle"}->{"pss:angle"};
       } elsif ($h->{"pss:fromAngle"}->{"pss:unit"} eq "RAD") {
           $phs[$nline] = $h->{"pss:fromAngle"}->{"pss:angle"}*(57.295779513082323);
       }
   }
   if (defined($h->{"pss:toAngle"})) {
       if ($h->{"pss:toAngle"}->{"pss:unit"} eq "DEG") {
           $phs[$nline] = -$h->{"pss:toAngle"}->{"pss:angle"};
       } elsif ($h->{"pss:toAngle"}->{"pss:unit"} eq "RAD") {
           $phs[$nline] = -$h->{"pss:toAngle"}->{"pss:angle"}*(57.295779513082323);
       }
   }
}

# -----------------------------------------------------------------------
# function for determining branch shunt admittance
# -----------------------------------------------------------------------
sub lineshunt {

   my $h = $_[0];

   if (defined($h->{"pss:totalShuntY"})) {
       if ($h->{"pss:totalShuntY"}->{"pss:unit"} eq "PU") {
           $bshunt[$nline] = $h->{"pss:totalShuntY"}->{"pss:b"};
           $gshunt[$nline] = $h->{"pss:totalShuntY"}->{"pss:g"};
       } elsif ($h->{"pss:totalShuntY"}->{"pss:unit"} eq "MICROMHO") {
           my $base = $linevb[$nline]*$linevb[$nline]/$linesb[$nline];
           $bshunt[$nline] = $h->{"pss:totalShuntY"}->{"pss:b"}*$base/1000000;
           $gshunt[$nline] = $h->{"pss:totalShuntY"}->{"pss:g"}*$base/1000000;
       }
   }
   if (defined($h->{"pss:toShuntY"})) {
       addshunt($h->{"pss:toShuntY"},$busto[$nline],$linesb[$nline],$linevb[$nline]/$kt[$nline]);
   }
   if (defined($h->{"pss:fromShuntY"})) {
       addshunt($h->{"pss:fromShuntY"},$busfr[$nline],$linesb[$nline],$linevb[$nline]);
   }
}
