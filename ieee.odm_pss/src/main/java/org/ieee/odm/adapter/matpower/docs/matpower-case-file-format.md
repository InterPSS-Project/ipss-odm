# MATPOWER Case File Format (Appendix B)

## Overview

MATPOWER uses a case struct (`mpc`) to store power system data.

- Format version: `2` (current)
- Stored as:
  - MATLAB `.m` file returning `mpc`
  - `.mat` file defining `mpc`

## Main Fields

- `mpc.version` (string, usually `"2"`)
- `mpc.baseMVA` (scalar)
- `mpc.bus`
- `mpc.branch`
- `mpc.gen`
- `mpc.gencost` (optional)

For the matrix fields:

- Rows represent elements such as buses, branches, or generators.
- Columns represent attributes defined by the MATPOWER format.

## Base MVA

### `mpc.baseMVA`

- Type: scalar
- Meaning: system power base in MVA

## Bus Data (`mpc.bus`)

| Col | Name | Description |
| --- | --- | --- |
| 1 | `bus_i` | Bus number |
| 2 | `type` | Bus type (`1=PQ`, `2=PV`, `3=ref`, `4=isolated`) |
| 3 | `Pd` | Real power demand (MW) |
| 4 | `Qd` | Reactive power demand (MVAr) |
| 5 | `Gs` | Shunt conductance (MW at `V = 1.0` p.u.) |
| 6 | `Bs` | Shunt susceptance (MVAr at `V = 1.0` p.u.) |
| 7 | `area` | Area number |
| 8 | `Vm` | Voltage magnitude (p.u.) |
| 9 | `Va` | Voltage angle (degrees) |
| 10 | `baseKV` | Base voltage (kV) |
| 11 | `zone` | Loss zone |
| 12 | `maxVm` | Maximum voltage magnitude (p.u.) |
| 13 | `minVm` | Minimum voltage magnitude (p.u.) |

## Generator Data (`mpc.gen`)

| Col | Name | Description |
| --- | --- | --- |
| 1 | `bus` | Bus number |
| 2 | `Pg` | Real power output (MW) |
| 3 | `Qg` | Reactive power output (MVAr) |
| 4 | `Qmax` | Maximum reactive power (MVAr) |
| 5 | `Qmin` | Minimum reactive power (MVAr) |
| 6 | `Vg` | Voltage setpoint (p.u.) |
| 7 | `mBase` | Machine base MVA |
| 8 | `status` | `1` = in service, `0` = out of service |
| 9 | `Pmax` | Maximum real power (MW) |
| 10 | `Pmin` | Minimum real power (MW) |

Additional optional columns may exist depending on the use case.

## Branch Data (`mpc.branch`)

| Col | Name | Description |
| --- | --- | --- |
| 1 | `fbus` | From bus |
| 2 | `tbus` | To bus |
| 3 | `r` | Resistance (p.u.) |
| 4 | `x` | Reactance (p.u.) |
| 5 | `b` | Line charging susceptance (p.u.) |
| 6 | `rateA` | Rating A (MVA) |
| 7 | `rateB` | Rating B (MVA) |
| 8 | `rateC` | Rating C (MVA) |
| 9 | `ratio` | Transformer off-nominal turns ratio |
| 10 | `angle` | Phase shift angle (deg) |
| 11 | `status` | `1` = in service |
| 12 | `angmin` | Minimum angle difference (deg) |
| 13 | `angmax` | Maximum angle difference (deg) |

## Generator Cost Data (`mpc.gencost`) (Optional)

Two formats are supported.

### 1. Polynomial Cost

| Col | Name | Description |
| --- | --- | --- |
| 1 | `model` | `2` = polynomial |
| 2 | `startup` | Startup cost |
| 3 | `shutdown` | Shutdown cost |
| 4 | `n` | Number of coefficients |
| 5+ | `c(n-1) ... c0` | Cost coefficients |

### 2. Piecewise Linear Cost

| Col | Name | Description |
| --- | --- | --- |
| 1 | `model` | `1` = piecewise linear |
| 2 | `startup` | Startup cost |
| 3 | `shutdown` | Shutdown cost |
| 4 | `n` | Number of points |
| 5+ | `(x1,y1) ... (xn,yn)` | Cost points |

## Notes

- Version 1 format is deprecated.
- Version 1 stored variables separately instead of using the `mpc` struct.
- MATPOWER provides helper constants:
  - `idx_bus`
  - `idx_brch`
  - `idx_gen`
  - `idx_cost`
- Additional optional fields may appear, such as OPF extensions.