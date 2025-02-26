public class Address {
    public int addr;
    public int line;

    public int DM_2KB_1WB_tag;
    public int DM_2KB_1WB_index;
    public int DM_2KB_2WB_index;
    public  int DM_2KB_2WB_tag;
    public int DM_2KB_4WB_tag;
    public int DM_2KB_4WB_index;
    public int TwoWay_2KB_1WB_index;
    public int TwoWay_2KB_1WB_tag;

    public int DM_4KB_1WB_index;
    public int DM_4KB_1WB_tag;

    public int FourWay_2KB_1WB_index;
    public int FourWay_2KB_1WB_tag;

    public int FourWay_2KB_4WB_index;
    public int FourWay_2KB_4WB_tag;

    public Address(int addr, int line){
        this.addr = addr;
        this.line = line;
        parseAddress();
    }

    private void parseAddress(){
        /*  2KB, direct mapped, 1-word blocks
            2KB, direct mapped, 2-word blocks
            2KB, direct mapped, 4-word blocks
            2KB, 2-way set associative, 1-word blocks
            2KB, 4-way set associative, 1-word blocks
            2KB, 4-way set associative, 4-word blocks
            4KB, direct mapped, 1-word blocks
         */

        // DM_2KB_1WB
        // Address = [ tag 21bits | index 9bits | byte offset 2bits ]
        DM_2KB_1WB_index = ( this.addr >> 2 ) & 0x1FF ;

        DM_2KB_1WB_tag   = ( this.addr >>> 11);
        // DM_2KB_2WB
        // Address = [ tag 21bits | index 8bits | blk offset 1bit | byte offset 2bits ]
        DM_2KB_2WB_index = ( this.addr >> 3 ) & 0xFF ;
        DM_2KB_2WB_tag   = ( this.addr >>> 11);
        // DM_2KB_4WB
        // Address = [ tag 21bits | index 7bits | blk offset 2bits | byte offset 2bits ]
        DM_2KB_4WB_index = ( this.addr >> 4 ) & 0x7F ;
        DM_2KB_4WB_tag   = ( this.addr >>> 11);

        // TwoWay_2KB_1WB
        // Address = [ tag 22bits | index 8bits  | byte offset 2bits ]
        TwoWay_2KB_1WB_index = ( this.addr >> 2 ) & 0xFF;
        TwoWay_2KB_1WB_tag = ( this.addr >>> 10);

        // DM_4KB_1WB
        // Address = [tag 20bits | index 10 bits | byte offset 2 bits ]
        DM_4KB_1WB_index = ( this.addr >> 2) & 0x3FF;
        DM_4KB_1WB_tag   = ( this.addr >>> 12);

        // FourWay_2KB_1WB
        // Address = [ tag 23 bits | index 7 bits | byte offset 2 bits ]
        FourWay_2KB_1WB_index = ( this.addr >> 2) & 0x7F;
        FourWay_2KB_1WB_tag = ( this.addr >>> 9);

        // FourWay_2KB_4WB
        // Address = [ tag 23 bits | index 5 bits | block offset 2 bits | byte offset 2 bits ]
        FourWay_2KB_4WB_index = ( this.addr >> 4) & 0x1F;
        FourWay_2KB_4WB_tag = ( this.addr >>> 9);
    }
}
