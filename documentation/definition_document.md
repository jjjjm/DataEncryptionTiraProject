# Project definition
This project will implement some data encryption algorithms and compare the performance of those algorithms (both time and space complexity).
The algorithms that will be implemented are ~~DES (Data Encryption Standard)~~ RC5 (Rivest Ciher 5), and RC4 (Rivest Cipher 4), possibly others if allotted time allows it (AES, some public key algorithm such as RSA). Main idea is to compare block cipher vs stream cipher.
## Algorithms and Data structures  
  Both algorithms mostly use array operations and byte-level manipulation to achieve the designed output.

## Input / Output  
  The input file is any file and output is encrypted version of input file.

## Sources
* An Introduction to Cryptography, Richard A. Mollin
* Data Encryption Standard (DES), Cleveland University
* Ronald L. Rivest, The RC5 Encryption Algorithm
* An Owerview of Cryptography, Gary C. Kessler

## Time-Complexity   
### RC4:
RC4 has variable key size which is processed to with every round of the cipher function. But since it's only stored in a constatnt size array after the first scheduling the size of the key doesn't matter after the initial scheduling.  So the time complexity simply would be O(n + k) with k being the key length and n being the length of the ciphered input, both in bits.
### RC5:  
RC5 is similar to RC4, although the initial key scheduling and plain text ciphering are also dependant on the given amount of rounds. But since that is constant in all the algorithm implementations (that is, it's supposed to be written to the code like a "magic number") it doesn't make difference. So the time complexity also comes down to O(n+k), k = key size, n = input size.
### DES:
~~ Since DES only runs constant time functions to each 64-bit block that do not scale at all with the input since the input is constant. So every block is processed in O(1) time and the whole algorithm is O(n) with n being the amount of 64-bit blocks in the input to the DES algorithm. ~~
