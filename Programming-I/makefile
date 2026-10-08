# Makefile for Ouri

CC = gcc #macro for C Compiler
DEPS = ouri_func.h #macro for .h file on which the .c files depend

ouri: ouri_main.o ouri_func.o  		#The executable depends on the object files
	$(CC) -o $@ $^					

%.o: %.c $(DEPS)  					#The object files depend on the C files and the header file
	$(CC) -c -o $@ $<

clean:								#Clean up the directory from object files and the executable.
	rm -f *.o ouri

