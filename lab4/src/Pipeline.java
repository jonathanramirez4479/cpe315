public class Pipeline {

    public boolean skip_lw = false;
    public boolean skip_execution = false;

    public Instructions[] pipeReg = new Instructions[4]; // pipeline registers
    public Pipeline(){
        initPipeReg(); // on new object, init pipeline
    }

    private void initPipeReg(){ // Fill with "empty" instructions
        pipeReg[0] = new Instructions("empty", null, 0);
        pipeReg[1] = new Instructions("empty", null, 0);
        pipeReg[2] = new Instructions("empty", null, 0);
        pipeReg[3] = new Instructions("empty", null, 0);
    }

    public void showPipe() {
        System.out.println("pc      if/id   id/exe  exe/mem mem/wb");
        System.out.println(lab4.counter + "      " + pipeReg[0].instruction + "    " + pipeReg[1].instruction
                + "     " + pipeReg[2].instruction + "     " + pipeReg[3].instruction);
    }

    public void addInstr(Instructions instr){
        // Adds instruction to pipeline, printed in next iteration.
        boolean special = false;

        if(skip_lw){
            pipeReg[3] = pipeReg[2];
            pipeReg[2] = pipeReg[1];
            // place stall without updating next instr
            pipeReg[1] = new Instructions("stall", null, 0);
            special = true;
        }
        if (pipeReg[2].branch_taken) {
            pipeReg[3] = pipeReg[2];
            pipeReg[2] = new Instructions("squash", null, 0);
            pipeReg[1] = new Instructions("squash", null, 0);
            pipeReg[0] = new Instructions("squash", null, 0);
            lab4.counter = pipeReg[3].branch_loc; // Finally, jump to branch location
            skip_execution = false;
            special = true;
        }

        if(pipeReg[0].getType().equals("J") || pipeReg[0].instruction.equals("jr")) {
            pipeReg[3] = pipeReg[2];
            pipeReg[2] = pipeReg[1];
            pipeReg[1] = pipeReg[0];
            pipeReg[0] = new Instructions("squash", null, 0);
            skip_execution = false;
            special = true;
        }
        if(!special){
            pipeReg[3] = pipeReg[2];
            pipeReg[2] = pipeReg[1];
            pipeReg[1] = pipeReg[0];
            pipeReg[0] = instr;
        }
    }

    public void checkPipe(){ // Once insert is complete, this will check values
        //Check if use-after-load
        if(pipeReg[1].lw_stall) {
            System.out.println("use-after load");
            skip_lw = true; // Skip next instruction
        }
        // Once lw in exe/mem, the stall is done and we can continue
        if(pipeReg[2].instruction.equalsIgnoreCase("lw")){
            skip_lw = false;
        }
        if(pipeReg[0].branch_taken){
            skip_execution = true;
        }
        if(pipeReg[0].getType().equals("J") || pipeReg[0].instruction.equals("jr")){// if J type instruction
            skip_execution = true;
        }
        if(pipeReg[1].getType().equals("J") || pipeReg[1].instruction.equals("jr")){// if J type instruction
                lab4.counter = pipeReg[1].jump_loc;
                if(pipeReg[1].instruction.equals("jr")){
                    lab4.counter ++;
                }
            }






    }
}
