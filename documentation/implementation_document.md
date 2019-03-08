# Implementation document
## Program structure
The program will use the classes that implement the Encryption interface to encrypt data given by the FileHandler class from any file. The files and encryption keys will be given to the encyption class by a CLI through the main program.
Both RC4 and RC5 use bit level manipulation of integers/longs and arrays to achieve the desired output.
The FileHandler class simply concatenates .RC4encrypted/.RC5enpcrypted to the given input file name while creating new file and then removes that string if you're decrypting.  

All the encryption algorithms implement the Encryption interface which makes the readability a bit easier and also helps with the passing of data to the algorithms.

![Data Flowchart](https://github.com/jjjjm/DataEncryptionTiraProject/blob/master/documentation/pics/flow.png)
## Achieved complexities.
RC4 is O(m + n) where m = key size in bits and n = input size of the data being handled in bits. Since it only do constant amount of operations to the given input byte, this was quite easily achieved in the program. Also it does given manipulations to the given key when it's first given it can be easily seen also that this was achieved in the program.  
RC5 has the same complexity
### Space complexities.  
The algorithms themselves run in constant space. That can be quite easily seen from the pseudo codes of the algorithms. Both RC4 and RC5 have a constant amount of key indexes so the themselves run in O(1), and the whole program runs in O(n) where n = input file size.

## Problems with the program.
I could not finish DES in time so its not implemented in actual runnable program. Also RC5 may sometimes crash the program if the given password is too short. RC5 sometimes doesn't encrypt/decrypt properly since the FileHandler passes bytes and RC5 wants 64bits as input (4bytes), so if the file size isn't the right size problems may occur.

## Sources  
https://www.cybrary.it/0p3n/des-data-encryption-standard/  
https://en.wikipedia.org/wiki/Data_Encryption_Standard
https://en.wikipedia.org/wiki/RC4
https://en.wikipedia.org/wiki/RC5
The original Ronald Rivest's RC5 paper: The RC5 Encryption Algorithm.
