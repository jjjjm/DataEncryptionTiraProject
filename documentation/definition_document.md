# Project definition
This project will implement some data encryption algorithms and compare the performance of those algorithms (both time and space complexity).
The algorithms that will be implemented are DES (Data Encryption Standard), and RC4 (Rivest Cioher 4), possibly others if allotted time allows it (AES, some public key algorithm such as RSA).    
## Algorithms and Data structures  
  Both algorithms mostly use array operations and byte-level manipulation to achieve the designed output. Both algorithms also have parity check for the keys used.

## Input / Output  
  The input file is any file and output is encrypted version of input file.

## Sources
* An Introduction to Cryptography, Richard A. Mollin
* Data Encryption Standard (DES), Cleveland University
* An Owerview of Cryptography, Gary C. Kessler

## Time-Complexity  
### DES:
Since DES only runs constant time functions to each 64-bit block that do not scale at all with the input since the input is constant. So every block is processed in O(1) time and the whole algorithm is O(n) with n being the amount of 64-bit blocks in the input to the DES algorithm.  
### RC4:
RC4 has variable key size which is processed to with every round of the cipher function. So the time complexity simply would be O(kn) with k being the key length and n being the length of the ciphered input, both in bits.
