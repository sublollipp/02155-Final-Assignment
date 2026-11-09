```
beq zero, zero, main

addfunc:
add a1, a2, a3
jalr x0, x1, 0

main:
li a2, 13
li a3, 54
la s0, addfunc
jalr x1, s0, 0
```