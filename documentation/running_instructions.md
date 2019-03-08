# Instructions  
Program can be run by running the jar with `java -jar encrypt-0.1.0.jar`.  The files that you want to use are supposed to be in the same folder as the jar.
Parameters must be passed to the program in the order:  
* File name
* Algorithm used (`RC4`,`RC5`)
* password to be used in the encryption/decryption (atleast 6 bytes, so atleast 6 characters)
* Mark if the file is encrypted (`e` if it is, anything else otherwise, NOTE can't be empty)  

So for example to encrypt the file `test.txt` make sure it exists in the same folder as jar.  
  
Then give command `java -jar encrypt-0.1.0.jar test.txt RC4 salasanasalasana d` and the program should create the file `test.txt.RC4encrypted` and print the time used and memory used.  
  
To decrypt the file give command `java -jar encrypt-0.1.0.jar test.txt.RC4encrypted RC4 salasanasalasana e` and the program should make(or overwrite) the file `test.txt` and print the time and memory used.  
  
The program accepts any file as input when encrypting and any file with the file extension `.RC4encrypted` or `.RC5encrypted` when decrypting (when the `e` flag is given).
