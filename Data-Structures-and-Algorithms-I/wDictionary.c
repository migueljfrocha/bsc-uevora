/*************************************************************************************************************
 * 
 * @authors: Miguel Pombeiro (57829) , Miguel Rocha (58501)
 * Spellchecker - Group Project
 * EDA1 2023/2024
 * 
 * @file wDictionary.c
 * 
 * This file contains the implementation of the dictionary structure and its functions.
 * The dictionary structure is an hash table used to store the words in the dictionary.
 * Uses the double hashing technique to solve collisions.
 * 
*************************************************************************************************************/


#include "spellChecker.h"

unsigned long collisions = 0;


/*************************************************************************************************************
 * 
 * This function creates a dictionary with a given size.
 * It allocates memory for the dictionary and for the entries array.
 * 
 * @param Size - the size of the dictionary
 * 
 * @return the dictionary
 *  
*************************************************************************************************************/
Dictionary* createDictionary(const unsigned long Size) {
    Dictionary* dict = (Dictionary*) malloc (sizeof (Dictionary));
    if (dict == NULL) {
        wprintf (L"Error allocating memory for dictionary");
        return NULL;
    }

    dict -> size = Size;
    dict -> nElements = 0;
    dict -> entries = (dictionaryEntry*) malloc (Size * sizeof (dictionaryEntry));
    if (dict -> entries == NULL) {
        wprintf (L"Error allocating memory for dictionary entries");
        return NULL;
    }

    return dict;
}


/*************************************************************************************************************
 * 
 * This function is used in case of collision.
 * This function calculates the index of a key in the dictionary using the double hashing technique.
 * It uses the djb2 function/algorithm and the hash2 function.
 * 
 * @param str - the key
 * @param dict - the dictionary
 * 
 * @return the index where the key should be in the dictionary
 *
*************************************************************************************************************/
Index doubleHashing (const entry *str, Dictionary* dict) {
    Index hashVal1 = djb2(str, dict->size);
    Index hashVal2 = hash2(str, dict->size);

    int i = 0;

    while (dict->entries[(hashVal1 + i * hashVal2)%dict->size].key != NULL)                         // If the index is already occupied continue searching
        i++;
    
    return (hashVal1 + i * hashVal2)%dict->size;
}

/*************************************************************************************************************
 * 
 * This function inserts a key in the dictionary.
 * It calculates the index of the key using the djb2 algorithm.
 * If the index is already occupied, it uses the double hashing technique to find another index.
 * 
 * @param dict - the dictionary
 * @param key - the key to be inserted
 * 
 * @return true if the key was inserted successfully, false otherwise
 *
*************************************************************************************************************/
bool insertEntry(Dictionary* dict, const entry* key) {
    if (dict == NULL) {
        wprintf(L"Dictionary not intialized");
        return false;
    }

    Index i = djb2(key, dict->size);                                                                // Index of the key

    if (dict->entries[i].key != NULL) {                                                             // If the index is already occupied, use double hashing
        collisions++;
        i = doubleHashing(key, dict);
    }

    // Insert the key in the dictionary

    int len = wcslen(key);                                                                          // Length of the key

    dict->entries[i].key = (entry*) malloc ((len+1) * sizeof (entry));
    if (dict->entries[i].key == NULL) {
        wprintf(L"Error allocating memory for key");
        return false;
    }

    if (wcscpy(dict->entries[i].key, key) == NULL) {                                                // Store the key to the dictionary
        wprintf(L"Error copying key");
        return false;
    }

    dict->entries[i].aValue = asciiValue(key);                                                      // Store the value of the key for faster search

    dict->nElements++;

    return true;
}

/*************************************************************************************************************
 * 
 * This function prints the number of collisions that occurred during the insertion of keys in the dictionary.
 * 
*************************************************************************************************************/
void printCollisions() {
    wprintf(L"Number of Collisions found while generating: %ld\n", collisions);
}


/*************************************************************************************************************
 * 
 * This function searches for a key in the dictionary.
 * It calculates the index of the key using the djb2 algorithm.
 * If the index is already occupied, it uses the double hashing technique to find another index.
 * 
 * @param dict - the dictionary
 * @param key - the key to be searched
 * 
 * @return true if the key was found, false otherwise
 *
*************************************************************************************************************/
bool searchEntry (const Dictionary* dict, const entry* key) {

    if (dict == NULL) {
        wprintf(L"Dictionary not intialized");
        return false;
    }

    if (key == NULL) {
        wprintf(L"Error: Key is empty");
        return false;
    }

    if (dict->nElements == 0)                                                                       // If the dictionary is empty, the key is not in the dictionary
        return false;
    
    unsigned long asciiVal = asciiValue(key);

    Index i, hashVal1, hashVal2;

    i = hashVal1 = djb2(key, dict->size);
    hashVal2 = hash2(key, dict->size);

    int j = 0;

    while (dict->entries[i].key != NULL) {                                                          // Search for the key in the dictionary
        if (dict->entries[i].aValue == asciiVal) {
            if (wcscmp(dict->entries[i].key, key) == 0)
                return true;
        }
        j++;
        i = (hashVal1 + j * hashVal2) % dict->size;
    }

    return false;
}


/*************************************************************************************************************
 * 
 * This function destroys the dictionary.
 * It frees the memory allocated for the keys and for the entries array.
 * 
 * @param dict - the dictionary to be destroyed
 * 
 * @return NULL
 *
*************************************************************************************************************/
Dictionary* destroyDictionary(Dictionary* dict) {
    if (dict == NULL){
        wprintf(L"Error destroying dictionary\n");
        return NULL;
    }

    for (unsigned long i = 0; i < dict->size; i++) {                                                // Free the memory allocated for the keys
        if (dict->entries[i].key != NULL) {
            free (dict->entries[i].key);
            dict->entries[i].key = NULL;
        }
    }

    free (dict->entries);
    dict->entries = NULL;
    dict->size = 0;
    dict->nElements = 0;

    free (dict);
    dict = NULL;

    return dict;
}