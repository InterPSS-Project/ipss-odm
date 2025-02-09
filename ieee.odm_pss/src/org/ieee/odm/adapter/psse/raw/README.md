#Raw to Rawx converter dev notes

## v0.1 
TODO:
- Need to update the transformer part to 1) differentiate three-winding and two-winding transformers; 2) need to handle missing data or add default data in RAW
- 4 lines for 2-winding, and only a subset of fields in these four lines are used. for example zcod in line#1 is not used
- 5 lines for 3-winding, all fields are used