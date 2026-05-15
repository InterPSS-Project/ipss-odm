# CIM/CGMES Import for InterPSS ODM — Development Summary

**Project:** InterPSS ODM (`org.ieee.odm.adapter.cim`)  
**Reference Implementation:** PowSyBl CGMES (`powsybl-core/cgmes`)  
**Status:** Complete through Phase 6 + CIMHub validation — **55 tests, all green**  
**Date:** 2025-05-15

---

## Table of Contents

1. [Overview](#1-overview)
2. [Architecture](#2-architecture)
3. [Class Reference](#3-class-reference)
4. [Data Flow](#4-data-flow)
5. [Bus Resolution Strategy](#5-bus-resolution-strategy)
6. [CIM → ODM Element Mapping](#6-cim--odm-element-mapping)
7. [Per-Unit Conversion](#7-per-unit-conversion)
8. [Multi-Profile Merge](#8-multi-profile-merge)
9. [Boundary File Handling](#9-boundary-file-handling)
10. [Key Bugs Found and Fixed](#10-key-bugs-found-and-fixed)
11. [Testing](#11-testing)
12. [Test Results](#12-test-results)
13. [Future Work](#13-future-work)

---

## 1. Overview

This module implements IEC 61970 CIM/CGMES import for InterPSS's Open Data Model (ODM). It converts CIM RDF/XML files into ODM's JAXB-based loadflow data model (`AclfModelParser`), following the same adapter pattern used by InterPSS's existing PSS/E, IEEE CDF, BPA, UCTE, and MATPOWER importers.

The implementation targets **load flow analysis** — converting buses, branches (lines, transformers), loads, generators, and shunt compensators. It draws architectural inspiration from PowSyBl's CGMES conversion system but adapts to InterPSS's stateless adapter pattern.

### Supported CIM Formats

| Format | Namespace | Topology | Files | Example |
|--------|-----------|----------|-------|---------|
| ENTSO-E CGMES (CIM16) | `http://iec.ch/TC57/2013/CIM-schema-cim16#` | TopologicalNode (bus-branch) | EQ + TP + SSH + SV + BD | MicroGrid, MiniGrid, SmallGrid |
| CIMHub (CIM100) | `http://iec.ch/TC57/CIM100#` | ConnectivityNode (node-breaker) | Single XML file | IEEE 118, WECC 240 |
| EQ-only (CIM16) | CIM16 | BusbarSection (node-breaker) | EQ only | T2_BE_EQ.xml |

---

## 2. Architecture

```
                    ┌──────────────────┐
                    │   CIMAdapter     │  Entry point
                    │ (extends         │  (AbstractODMAdapter)
                    │  AbstractODM     │
                    │  Adapter)        │
                    └────────┬─────────┘
                             │
                    ┌────────▼─────────┐
                    │  CIMRdfParser    │  RDF/XML → Jena Model
                    └────────┬─────────┘
                             │
                    ┌────────▼─────────┐
                    │    CIMModel      │  Wraps Jena Model
                    │  + CIMConstants  │  Typed element access
                    │  + CIMPropertyBag│  Topology indices
                    └────────┬─────────┘
                             │
         ┌───────────────────┼───────────────────┐
         │                   │                   │
    ┌────▼────┐        ┌─────▼─────┐       ┌─────▼──────┐
    │Container│        │  Branch   │       │ Injection  │
    │Mappers  │        │  Mappers  │       │  Mappers   │
    ├─────────┤        ├───────────┤       ├────────────┤
    │Substation       │LineMapper │       │LoadMapper  │
    │Mapper   │        │XfrMapper  │       │GenMapper   │
    │VoltageLevel     │Xfr3WMapper│       │ShuntMapper │
    │Mapper   │        └─────┬─────┘       └─────┬──────┘
    └─────────┘              │                   │
                    ┌────────▼───────────────────▼──┐
                    │      AclfModelParser          │
                    │  (ODM JAXB loadflow model)    │
                    └──────────────────────────────┘
```

### Design Principles

1. **Stateless adapter** — Parse once, produce model, done. No database or persistent state.
2. **Apache Jena over RDF4J** — Lighter-weight, sufficient for one-shot RDF/XML parsing with SPARQL support.
3. **Mapper pattern** — One mapper per CIM equipment type, all extend `AbstractCIMDataMapper`.
4. **PropertyBag accessor** — Wraps Jena `Resource` with typed getters (mirrors PowSyBl's `PropertyBag`).
5. **Auto-detection** — CIM version (CIM16/CIM100) and topology model (bus-branch/node-breaker) detected automatically.

---

## 3. Class Reference

### Package: `org.ieee.odm.adapter.cim`

| Class | Lines | Role |
|-------|-------|------|
| **CIMAdapter** | 386 | Entry point. Extends `AbstractODMAdapter`. Orchestrates parsing and conversion pipeline. Handles single-file and multi-file input. |
| **CIMConstants** | 145 | Namespace URIs (`CIM16_NS`, `CIM100_NS`, `ENTSOE_NS`) and property name constants. |
| **CIMModel** | 540 | Wraps Jena `Model`. Provides typed element list access, topology index building, boundary node detection, bus ID mapping, voltage lookup, and SPARQL execution. |
| **CIMPropertyBag** | 195 | Wraps a Jena `Resource`. Typed property accessors: `getId()`, `getLocalId()`, `getName()`, `getDouble()`, `getResourceId()`, etc. |

### Package: `org.ieee.odm.adapter.cim.mapper`

| Class | Lines | Role |
|-------|-------|------|
| **AbstractCIMDataMapper** | 64 | Base class. Provides `resolveBusId(equipmentId)` and `resolveBranchBusIds(equipmentId)` for terminal → bus resolution. All mappers extend this. |
| **CIMSubstationMapper** | 42 | Maps `Substation` → ODM area grouping (currently placeholder). |
| **CIMVoltageLevelMapper** | 30 | Maps `VoltageLevel` → nominal voltage info (used during bus creation). |
| **CIMLineMapper** | 172 | Maps `ACLineSegment` → `LineBranchXmlType` and `SeriesCompensator` → line. Handles R/X/Gch/Bch → PU conversion with base voltage resolution. |
| **CIMTransformerMapper** | 176 | Maps 2-winding `PowerTransformer` + 2 `TransformerEnds` → `XfrBranchXmlType`. Impedance from ends, turn ratio from ratedU, from/to bus from terminals. |
| **CIMTransformer3WMapper** | 254 | Maps 3-winding `PowerTransformer` + 3 `TransformerEnds` → `Xfr3WBranchXmlType` using star-bus equivalent model (z₁₂=z₁+z₂, z₂₃=z₂+z₃, z₃₁=z₃+z₁). |
| **CIMLoadMapper** | 92 | Maps `EnergyConsumer` and `AsynchronousMachine` → bus load data. Accumulates P/Q when multiple loads on same bus. |
| **CIMGeneratorMapper** | 214 | Maps `SynchronousMachine` → PV/SWING/PQ generator, `ExternalNetworkInjection` → SWING generator. Uses `GeneratingUnit.initialP`, `RegulatingControl.targetValue` for setpoints. |
| **CIMShuntCompensatorMapper** | 145 | Maps `LinearShuntCompensator` and `NonlinearShuntCompensator` → bus shunt admittance. Handles section-based B/G calculation with fallback for nonlinear shunts. |

### Package: `org.ieee.odm.adapter.cim.parser`

| Class | Lines | Role |
|-------|-------|------|
| **CIMRdfParser** | 86 | Loads RDF/XML string(s) into Jena `Model`. Applies pre-processing: (1) strips malformed UUID suffixes, (2) normalizes `md:FullModel rdf:about` base URIs to `http://cim.import/base`. |

### Package: `org.ieee.odm.adapter.cim.util`

| Class | Lines | Role |
|-------|-------|------|
| **CIMUnitConverter** | 88 | Unit conversion utilities (Ohm/Siemens → PU, MW → PU). |

### Total: 2,629 lines of production code across 15 classes.

---

## 4. Data Flow

```
  CIM RDF/XML files (EQ, TP, SSH, SV, BD)
          │
          ▼
  ┌───────────────────────────────────┐
  │ CIMRdfParser.parseString()        │
  │  1. Strip bad UUID suffixes       │
  │  2. Normalize FullModel base URI  │
  │  3. Jena Model.read(RDF/XML)      │
  │  4. Union-merge all files         │
  └───────────────┬───────────────────┘
                  │
                  ▼
  ┌───────────────────────────────────┐
  │ CIMModel(jenaModel)               │
  │  • detectNamespace() → CIM16/CIM100│
  │  • buildIndices():                 │
  │    - Terminal → Equipment map      │
  │    - Terminal → TopoNode map       │
  │    - Terminal → ConnNode map       │
  │    - BaseVoltage index (V→kV norm) │
  │    - Boundary TN detection         │
  │    - VL → Substation map           │
  │    - TopoNode → VL map             │
  │    - ConnNode → VL map             │
  └───────────────┬───────────────────┘
                  │
                  ▼
  ┌───────────────────────────────────┐
  │ CIMAdapter.buildOdmModel()        │
  │                                    │
  │  1. convertContainers()            │
  │     Substations, VoltageLevels     │
  │                                    │
  │  2. convertBuses()                 │
  │     TopoNode | Busbar | ConnNode   │
  │     → AclfModelParser.createBus()  │
  │                                    │
  │  3. convertBranches()              │
  │     ACLineSegment → LineMapper     │
  │     SeriesCompensator → LineMapper │
  │     PowerTransformer → XfrMapper   │
  │     PowerTransformer(3end)→Xfr3W   │
  │                                    │
  │  4. convertInjections()            │
  │     EnergyConsumer → LoadMapper    │
  │     AsyncMachine → LoadMapper      │
  │     SyncMachine → GenMapper        │
  │     ExtNetworkInj → GenMapper      │
  │     ShuntCompensator → ShuntMapper │
  └───────────────┬───────────────────┘
                  │
                  ▼
         AclfModelParser (ODM)
         └→ JAXB XML model
```

---

## 5. Bus Resolution Strategy

CIM uses a **Terminal → Node** topology model. The adapter supports three tiers of bus resolution:

### Tier 1: TopologicalNode (bus-branch model)
Used when EQ + TP profiles are loaded. `TopologicalNode` is the CIM bus-branch equivalent of a bus.
```
Equipment → Terminal → TopologicalNode → Bus ID
```
Boundary TopologicalNodes (detected via `boundaryPoint=true`) are **skipped** — they represent external network connections.

### Tier 2: ConnectivityNode (node-breaker model)
Fallback when no TopologicalNodes exist. Each `ConnectivityNode` becomes a bus.
```
Equipment → Terminal → ConnectivityNode → Bus ID
```
Used by CIMHub IEEE 118 / WECC 240 (pure node-breaker models).

### Tier 3: BusbarSection (EQ-only)
Fallback when no TopologicalNodes AND no ConnectivityNode terminal references. BusbarSections serve as bus proxies.
```
BusbarSection → Bus ID
Other equipment → Terminal → ConnectivityNode → BusbarSection → Bus ID
```

### Resolution in `AbstractCIMDataMapper`

All mappers use `resolveBusId(equipmentId)` which:
1. Gets terminals for equipment from `CIMModel.getTerminalsForEquipment()`
2. For each terminal, tries `topologicalNodeByTerminal` first
3. Falls back to `connectivityNodeByTerminal`
4. Looks up `busIdByTopoNode` map (populated during bus creation)
5. Returns `null` if bus wasn't created (e.g., boundary node) → element skipped

---

## 6. CIM → ODM Element Mapping

| CIM Element | CIM Class | ODM Target | Mapper | Notes |
|-------------|-----------|------------|--------|-------|
| Substation | `Substation` | Area grouping | `CIMSubstationMapper` | Sequential area numbering |
| Voltage Level | `VoltageLevel` | Bus base voltage | `CIMVoltageLevelMapper` | `nominalVoltage` → kV |
| Topological Node | `TopologicalNode` | `LoadflowBusXmlType` | (in CIMAdapter) | Skip boundary TNs |
| Connectivity Node | `ConnectivityNode` | `LoadflowBusXmlType` | (in CIMAdapter) | Tier 2 fallback |
| Busbar Section | `BusbarSection` | `LoadflowBusXmlType` | (in CIMAdapter) | Tier 3 fallback |
| AC Line Segment | `ACLineSegment` | `LineBranchXmlType` | `CIMLineMapper` | R/X/Gch/Bch → PU |
| Series Compensator | `SeriesCompensator` | `LineBranchXmlType` | `CIMLineMapper` | R/X only, no shunt |
| Power Transformer (2W) | `PowerTransformer` + 2 ends | `XfrBranchXmlType` | `CIMTransformerMapper` | From-end impedance |
| Power Transformer (3W) | `PowerTransformer` + 3 ends | `Xfr3WBranchXmlType` | `CIMTransformer3WMapper` | Star-bus equivalent |
| Energy Consumer | `EnergyConsumer` | Bus load data | `CIMLoadMapper` | P/Q from SSH, accumulate |
| Asynchronous Machine | `AsynchronousMachine` | Bus load data | `CIMLoadMapper` | Motor → load (PowSyBl) |
| Synchronous Machine | `SynchronousMachine` | Bus gen data | `CIMGeneratorMapper` | PV/SWING/PQ classification |
| External Network Inj. | `ExternalNetworkInjection` | SWING gen | `CIMGeneratorMapper` | Equivalent infinite bus |
| Linear Shunt | `LinearShuntCompensator` | Bus shunt Y | `CIMShuntCompensatorMapper` | bPerSection × sections |
| Nonlinear Shunt | `NonlinearShuntCompensator` | Bus shunt Y | `CIMShuntCompensatorMapper` | Point-based B/G lookup |
| Boundary ACLineSegment | (deferred) | Not created | — | Boundary TN → skip |

---

## 7. Per-Unit Conversion

All impedance/admittance values are converted from physical units (Ω, S) to per-unit at the mapper level.

### Formulas

```
baseZ = baseKV² / baseMVA    (Ω, where baseKV is in kV)
baseY = baseMVA / baseKV²    (S)

rPU = r_ohm / baseZ
xPU = x_ohm / baseZ
gPU = g_siemens / baseY
bPU = b_siemens / baseY
```

### Base Voltage Resolution

`baseKV` is determined for each element by:
1. `ConductingEquipment.BaseVoltage` → `BaseVoltage.nominalVoltage` (preferred)
2. Connected TopologicalNode → VoltageLevel → nominal voltage
3. Default 100 kV (with warning)

### Voltage Normalization

CIM spec says `BaseVoltage.nominalVoltage` is in **Volts**, but ENTSO-E data uses **kV**. The adapter normalizes: if value > 1000, divide by 1000.

---

## 8. Multi-Profile Merge

CGMES uses separate files for different profiles (EQ, TP, SSH, SV, BD). The adapter merges them using Jena's `Model.add()`:

```java
Model merged = ModelFactory.createDefaultModel();
for (IFileReader reader : dins) {
    Model part = rdfParser.parseString(content);
    merged.add(part);
}
```

### XML Base URI Normalization (Critical Fix)

Each CGMES file has `<md:FullModel rdf:about="urn:uuid:...">` which sets a different XML base URI. This causes `rdf:ID`/`rdf:about` references to resolve to different absolute URIs per file — the same terminal in EQ vs TP becomes **separate Jena resources**.

**Fix:** `CIMRdfParser` regex-replaces `md:FullModel rdf:about="urn:uuid:..."` with a stable base `http://cim.import/base` before Jena parsing. All files then share the same base URI.

---

## 9. Boundary File Handling

Boundary files (EQ_BD + TP_BD) define inter-TSO interfaces:

- **Boundary ConnectivityNodes** have `boundaryPoint=true` (entsoe namespace)
- **Boundary TopologicalNodes** are detected and **not created as buses**
- ACLineSegments with one terminal at a boundary TN are skipped (would become DanglingLines in PowSyBl)
- EquivalentInjections at boundary nodes are not imported (external network)

---

## 10. Key Bugs Found and Fixed

### 10.1 XML Base URI Normalization
**Problem:** Multi-file merge fails — same resource has different URIs across files.  
**Fix:** Regex in `CIMRdfParser` normalizes all `md:FullModel` base URIs to `http://cim.import/base`.

### 10.2 Phantom Branch Bug
**Problem:** `AbstractModelParser.initBranchData()` adds branch to XML list BEFORE `addBranch2BaseCase()` duplicate check. Duplicate exception leaves orphan branch in list.  
**Fix:** Reordered: `addBranch2BaseCase()` (check) → `initBranchData()` (add). Applied to ALL branch creation methods (`createLineBranch`, `createXfrBranch`, `createXfr3WBranch`, `createPSXfrBranch`, `createPSXfr3WBranch`).

### 10.3 Load Accumulation
**Problem:** Multiple loads on same bus → `AclfDataSetter.setLoadData()` overwrites previous values.  
**Fix:** Read existing P/Q, accumulate, then set totals.

### 10.4 Base Voltage Unit Mismatch
**Problem:** `BaseVoltage.nominalVoltage` can be V (CIM spec) or kV (ENTSO-E convention). Formula `baseKV² × 1000 / baseMVA` was 1000× too large for kV values.  
**Fix:** (1) Heuristic normalization: if > 1000, divide by 1000. (2) Correct formula: `baseZ = baseKV² / baseMVA`.

### 10.5 CIM100 Namespace Detection
**Problem:** Namespace detection only checked for `Substation`, which doesn't exist in node-breaker models.  
**Fix:** Check multiple common types (ACLineSegment, ConnectivityNode, EnergyConsumer).

### 10.6 NonlinearShuntCompensator Section Mismatch
**Problem:** `normalSections=1` but first point at `sectionNumber=2` → no match → b=0 → shunt skipped.  
**Fix:** Fallback to minimum section number's B/G values.

### 10.7 AsynchronousMachine Handling
**Problem:** Not imported (no mapper). PowSyBl always creates a Load for AsyncMachine.  
**Fix:** Added AsyncMachine processing in `CIMLoadMapper`, skipping zero-load check.

---

## 11. Testing

### Test Files (12 classes, 55 @Test methods)

| Test Class | Tests | What It Validates |
|------------|-------|-------------------|
| `CIMParseTest` | 1 | RDF/XML parsing produces valid Jena Model |
| `CIMAdapterTest` | 7 | Basic adapter functionality, edge cases |
| `CIMAdapterPhase1Test` | 5 | Foundation: namespace detection, element queries |
| `CIMAdapterPhase2Test` | 5 | Bus-branch: buses from TopologicalNodes, lines, transformers |
| `CIMAdapterPhase3Test` | 6 | Injections: loads, generators (PV/SWING), shunts |
| `CIMAdapterPhase4Test` | 5 | Multi-file merge (EQ+TP+SSH+SV) |
| `CIMAdapterPhase5Test` | 3 | 3-winding transformers |
| `CIMAdapterPhase6Test` | 4 | ENTSO-E conformity: element counts vs PowSyBl reference |
| `CIMAdapterAdditionalTest` | 9 | Additional edge cases, boundary handling |
| `CIMAdapterLargerTest` | 6 | Larger test cases (MiniGrid, SmallGrid) |
| `CIMCIMHubTest` | 2 | IEEE 118 + WECC 240 with MATPOWER cross-validation |
| `CIMDiagTest` | 2 | Diagnostic tests for debugging |

### Test Data

| File | Source | Size |
|------|--------|------|
| `MicroGrid_T4_BE_*_V2.xml` (6 files: EQ, TP, SSH, SV, EQ_BD, TP_BD) | ENTSO-E conformity | ~150 KB |
| `MiniGrid_NB_*_V3.xml` (6 files) | ENTSO-E conformity | ~500 KB |
| `SmallGrid_BB_*_V3.xml` (6 files) | ENTSO-E conformity | ~2 MB |
| `IEEE118_CIM.xml` + `IEEE118.m` | CIMHub BES (CIM100 + MATPOWER) | 1.5 MB + 53 KB |
| `WECC240_CIM.xml` + `WECC240.m` | CIMHub BES (CIM100 + MATPOWER) | 2.5 MB + 85 KB |
| `T2_*` (EQ, TP, SSH for BE and NL) | ENTSO-E test cases | ~30 KB |
| `SmallGrid_HVDC_*_V3.xml` (4 files) | ENTSO-E HVDC test | ~200 KB |

---

## 12. Test Results

### Phase 6: ENTSO-E Conformity (vs PowSyBl CimStatsTest)

| Test Case | buses | lines | xfr2w | xfr3w | loads | gens | shunts | Status |
|-----------|-------|-------|-------|-------|-------|------|--------|--------|
| MicroGrid_T4_BE | 7 | 3 | 3 | 1 | 3 | 2 | 2 | ✅ PASS |
| MiniGrid_NB | 11 | 7 | 4 | 2 | 3 | 5 | 0 | ✅ PASS |
| SmallGrid_BB | 115 | 173 | 10 | 0 | 103 | 19 | 14 | ✅ PASS |

**3/3 PASS** — element counts match PowSyBl CGMES file import exactly.

### CIMHub Cross-Validation (vs MATPOWER)

| Case | Format | buses | lines | xfr2w | loads | gens | shunts |
|------|--------|-------|-------|-------|-------|------|--------|
| IEEE 118 | CIM (ours) | 193 | 170 | 84 | 99 | 56 | 14 |
| IEEE 118 | MATPOWER | 193 | 170 | 84 | 99 | 54 | 14 |
| WECC 240 | CIM (ours) | 243 | 329 | 122 | 139 | 49 | 7 |
| WECC 240 | MATPOWER | 243 | 329 | 122 | 139 | 57 | 5 |

**PU validation:** IEEE 118 Line 1_2_1: r=0.030300, x=0.099900 — matches MATPOWER to 6 decimal places.

Gen/shunt differences: CIM has 111 SynchronousMachines on 49 unique CN buses (111 SM → 49 gen buses); MATPOWER has 292 gen entries on 57 buses. Shunt diff: CIM has 7 LinearShuntCompensators vs MATPOWER's 5 bus-shunt entries (different modeling conventions).

### Overall: **55 tests, 0 failures, BUILD SUCCESS**

---

## 13. Future Work

| Feature | Priority | Notes |
|---------|----------|-------|
| Tap changers (RatioTapChanger, PhaseTapChanger) | Medium | Data in EQ, positions in SSH |
| Operational limits (CurrentLimit, ActivePowerLimit) | Medium | PATL/TATL on branches |
| HVDC / DC equipment | Medium | SmallGrid_HVDC test data available |
| Phase-shifting transformer | Low | PSXfr branch type |
| SV profile voltage/power flow injection | Low | Post-load-flow state data |
| EquivalentInjection at boundary nodes | Low | External network modeling |
| Switch processing | Low | Node-breaker → bus-branch reduction |
| Power flow validation (solve imported model) | High | Verify PF convergence matches CGMES reference |

---

## Key Source Files

```
ieee.odm_pss/src/main/java/org/ieee/odm/adapter/cim/
├── CIMAdapter.java                    (386 lines) — Main adapter
├── CIMConstants.java                  (145 lines) — Namespaces, property names
├── CIMModel.java                      (540 lines) — Jena Model wrapper + indices
├── CIMPropertyBag.java                (195 lines) — Typed RDF resource accessor
├── mapper/
│   ├── AbstractCIMDataMapper.java     (64 lines)  — Base mapper, bus resolution
│   ├── CIMLineMapper.java             (172 lines) — Lines + SeriesCompensator
│   ├── CIMTransformerMapper.java      (176 lines) — 2W transformers
│   ├── CIMTransformer3WMapper.java    (254 lines) — 3W transformers (star-bus)
│   ├── CIMLoadMapper.java             (92 lines)  — Loads + AsyncMachine
│   ├── CIMGeneratorMapper.java        (214 lines) — Generators + ExtNetInjection
│   ├── CIMShuntCompensatorMapper.java (145 lines) — Linear + Nonlinear shunts
│   ├── CIMSubstationMapper.java       (42 lines)  — Substations
│   └── CIMVoltageLevelMapper.java     (30 lines)  — Voltage levels
├── parser/
│   └── CIMRdfParser.java              (86 lines)  — RDF/XML → Jena Model
└── util/
    └── CIMUnitConverter.java          (88 lines)  — Unit conversion
```
