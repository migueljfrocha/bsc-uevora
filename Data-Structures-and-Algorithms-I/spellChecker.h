/*************************************************************************************************************
 * 
 * @authors: Miguel Pombeiro (57829) , Miguel Rocha (58501)
 * Spellchecker - Group Project
 * EDA1 2023/2024
 * 
 * @file spellChecker.h
 * 
 * This is the header file for the dictionary and misspelling list structures and their functions.
 * It contains the prototypes of the functions and the structures used in the spellchecker project.
 * 
*************************************************************************************************************/


#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>
#include <wchar.h>

typedef unsigned long Index;
typedef wchar_t entry;

typedef struct dictionaryEntry misspell;
typedef struct Dictionary misspellList;

typedef struct dictionaryEntry suggestion;
typedef struct Dictionary suggestionList;


/*************************************************************************************************************
 * 
 * Dictionary Structure and it's functions
 * 
*************************************************************************************************************/

typedef struct dictionaryEntry {
    entry* key;                         // pointer to entry string
    unsigned long aValue;               // value of the sum of key's characters
} dictionaryEntry;

typedef struct Dictionary {
    unsigned long size;                 // size of the dictionary
    unsigned long nElements;            // number of elements in the dictionary
    dictionaryEntry* entries;           // pointer to the entries array (dictionary itself)
} Dictionary;

//create dictionary
Dictionary* createDictionary(const unsigned long nEntries);

//insert entry
bool insertEntry(Dictionary* dict, const entry* key);

//search entry
bool searchEntry(const Dictionary* dict, const entry* key);

//Double hashing
Index doubleHashing (const entry *str, Dictionary* dict);

//destroy dictionary
Dictionary* destroyDictionary(Dictionary* dict);

//print collision
void printCollisions();


/*************************************************************************************************************
 * 
 * Misspell List Structure and it's functions
 * 
*************************************************************************************************************/

//create misspelling list
misspellList* createTable();

//insert misspelling
misspellList* insertTable(misspellList* list, const entry* key);

//rehash misspelling list
misspellList* rehash(misspellList* list);

//search misspelling
bool searchTable(const misspellList* list, const entry* key);

//destroy misspelling list
misspellList* destroyTable(misspellList* list);


/*************************************************************************************************************
 * 
 * Auxiliar Functions
 * 
*************************************************************************************************************/

//Count number of words in the file
unsigned int countWords(FILE* file);

//next prime number
unsigned long nextPrime(unsigned long N);
//previous prime number
unsigned long prevPrime(unsigned long N);

//ascii value of the key
unsigned long asciiValue(const entry* key);

//hash functions
Index djb2(const entry *str, const unsigned long size);
Index hash2(const entry *str, const unsigned long size);


//Misspelling' Suggestions Functions
bool characterInsert(const Dictionary* dict, suggestionList** list, const entry* word, FILE* output);

bool characterDelete(const Dictionary* dict, suggestionList** list, const entry* word, FILE* output, int control);

bool characterSwap(const Dictionary* dict, suggestionList** list, const entry* word, FILE* output, int control);

bool characterReplace (const Dictionary* dict, suggestionList** list, const entry* word, FILE* output, int control);



/*************************************************************************************************************
 * 
 * Spell Checker Main Functions
 * 
*************************************************************************************************************/

// Read dictionary file and create dictionary
Dictionary* lerDicionario (const char* fileDic);

// Read text file and create misspelling list
misspellList* spellCheck (const char* fileTex, const char* fileOut, const Dictionary* dict);

// Print suggestions to the output file
bool geraOutput (const entry* word, const Dictionary* dict, FILE* output);
