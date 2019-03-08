# Testing document  
## What was tested
Testing is implemented with use of Java Systems-class' currentTimeMillis to compare time usage and Runtime totalMemory and freeMemory methods to compare time space usage between. The time is measured in Main class with it starting when the new FileHandler has been created and it ends when the program ends.  
Memory complexity is measured with simply running totalMemory - freeMemory between encryption steps and saved to a variable. It's only saved if it's larger than the current largest of this current run time of the program. So it gives the maximum memory used this runtime.  
## What was used in testing  
Although the program should be able to run with any given file, only .txt file were used to verify that the encryption was done correctly. So the files used in the test was empty .txt file, small .txt file (about 512KB) and large .txt file 6.5MB. These were repeated many time for the data.
## Real time and memory usages.
Chart of the averages of time spent encrypting  
![Encrypt Picture](https://github.com/jjjjm/DataEncryptionTiraProject/blob/master/documentation/pics/chart_time.png)  
Chart of the averages of time spent decrypting  
![Decrypt picture](https://github.com/jjjjm/DataEncryptionTiraProject/blob/master/documentation/pics/chart_time_decrypt.png)  
Memory usage stayed the same with almost every time running (about +/- 5KB variation). Also checked with OS system monitor the program memory usage it also stayed the same with every run. This is not unexpected since the algorithms themselves do not use any more space than assigned after the first use and also only one block of data is taken in to program memory from the file and written, only after that new block is read from the file.
## How to test  
Test can be reproduced by running the program and taking the data points, at least that's how the charts above were achieved. Each file size was tested 15 times then the average was calculated to get the data points.
