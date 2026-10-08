# Makefile for SpellChecker

# Compiler
CC = gcc

# Dependencies
DEPS = spellChecker.h		# Header files

spellchecker: spellChecker.o wDictionary.o wMisspells.o auxiliary.o	# .o files required to build spellchecker
	$(CC) -o $@ $^ -lm			# $^ is the list of dependencies

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $<

.PHONY: clean
clean:
	rm -f *.o spellchecker