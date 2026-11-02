public class RISC {

    static int pc;
    static int reg[] = new int[32];

    static int progr[] = {
        // As minimal RISC-V assembler example
        0x00200093, // addi x1 x0 2
        0x00300113, // addi x2 x0 3
        0x002081b3, // add x3 x1 x2
    };

    public static void dump() {
       for (int i = 0; i < reg.length; ++i) {
            System.out.print(reg[i] + " ");
        }
        System.out.println();
    }

    public static int toUnsigned(int signedval) {
        return (~signedval) + 1; // twos complement
    }

    public static void main(String[] args) {
        for (; ;) {
            int instr = progr[pc >> 2];
            int opcode = instr & 0x7f;
            
            // Setting all variables for different instruction parameters
            int rd = (instr >> 7) & 0x1f;
            int rs1 = (instr >> 15) & 0x1f;
            int rs2 = (instr >> 20) & 0x1f;
            int funct3 = (instr >> 12) & 0x7;
            int funct7 = (instr >> 25);
            int imm = 0;

            
            String majop = "undefined / unimplemented";
            switch (opcode >> 2) {
                case 0xc: // R-type
                    majop = "R";
                    if ((funct7 != 0x00 && funct7 != 0x20) || (funct7 == 0x20 && funct3 != 0x0 && funct3 != 0x5)) {
                        System.out.println("The instruction [" + String.valueOf(instr) + "] is not supported.");
                    }
                    switch (funct3) {
                        case 0x0:
                            switch (funct7) {
                                case 0x0: // ADD
                                    reg[rd] = reg[rs1] + reg[rs2];
                                    break;
                                case 0x20: // SUB
                                    reg[rd] = reg[rs1] - reg[rs2];
                                    break;
                            }
                            break;
                        case 0x4: // XOR
                            reg[rd] = reg[rs1] ^ reg[rs2];
                            break;
                        case 0x6: // OR
                            reg[rd] = reg[rs1] | reg[rs2];
                            break;
                        case 0x7: // AND
                            reg[rd] = reg[rs1] & reg[rs2];
                            break;
                        case 0x1: // Shift Left Logical
                            reg[rd] = reg[rs1] << reg[rs2];
                            break;
                        case 0x5: 
                            switch (funct7) {
                                case 0x0: // Shift Right Logical
                                    reg[rd] = reg[rs1] >>> reg[rs2];
                                    break;
                                case 0x20: // Shift right Arith (MSB extends)
                                    reg[rd] = reg[rs1] >> reg[rs2];
                                    break;
                            }
                        case 0x2: // Set Less Than
                            reg[rd] = (reg[rs1] < reg[rs2])?1:0;
                            break;
                        case 0x3: // Set Less Than (U)
                            reg[rd] = (toUnsigned(reg[rs1]) < toUnsigned(reg[rs2]))?1:0;
                            break;                            
                    }
                    break;
                case 0x8:
                    majop = "S";
                    imm = ((instr >>> 7) & 0x1f) + ((instr >>> 25) << 5);
                    break;
                case 0x18:
                    majop = "B";
                    imm = ((instr >>> 7) & 0x1) + (((instr >>> 8) & 0xf) << 1) + (((instr >>> 25) & 0x3f) << 5) + ((instr >>> 31) << 12);
                    break;
                case 0x1b:
                    majop = "J";
                    imm = (((instr >>> 12) & 0xff) << 12) + (((instr >>> 20) & 0x1) << 11) + (((instr >>> 21) & 0x3ff) << 1) + ((instr >>> 31) << 20);
                    break;
                case 0xd:
                case 0x5:
                    majop = "U";
                    imm = (instr >>> 12) << 12;
                    break;
                case 0x4:
                case 0x0:
                case 0x19:
                case 0x1c:
                    majop = "I";
                    imm = (instr >>> 20);
                    if (funct3 == 0x5 && imm >>> 5 != 0x00 && imm >>> 5 != 0x20) {
                        System.out.println("The instruction [" + String.valueOf(instr) + "] is not supported.");
                    }
                    switch (funct3) {
                        case 0x0:
                            switch (funct7) {
                                case 0x0: // ADD Immediate
                                    reg[rd] = reg[rs1] + imm;
                                    break;
                            }
                            break;
                        case 0x4: // XOR Immediate
                            reg[rd] = reg[rs1] ^ imm;
                            break;
                        case 0x6: // OR Immediate
                            reg[rd] = reg[rs1] | imm;
                            break;
                        case 0x7: // AND Immediate
                            reg[rd] = reg[rs1] & imm;
                            break;
                        case 0x1: // Shift Left Logical Imm
                            reg[rd] = reg[rs1] << imm;
                            break;
                        case 0x5: 
                            switch (imm >>> 5) {
                                case 0x00: // Shift Right Logical Imm
                                    reg[rd] = reg[rs1] >>> (imm & 0xf);
                                    break;
                                case 0x20: // Shift right Arith Imm (MSB extends)
                                    reg[rd] = reg[rs1] >> (imm & 0xf);
                                    break;
                            }
                        case 0x2: // Set Less Than Imm
                            reg[rd] = (reg[rs1] < imm)?1:0;
                            break;
                        case 0x3: // Set Less Than Imm (U)
                            reg[rd] = (toUnsigned(reg[rs1]) < toUnsigned(imm))?1:0;
                            break;                            
                    }
                    break;
                default:
                    System.out.println("Opcode " + opcode + " not yet implemented");
                    break;
           }

           System.out.println("Major Opcode: " + majop);
           System.out.println("rd: " + String.valueOf(rd));

            pc += 4;

            reg[0] = 0;
            if ((pc >> 2) >= progr.length) {
                break;
            }
        }
        dump();
    }
    

}