/*************************************************************************************************************
 * 
 * @authors: Miguel Pombeiro (57829) , Miguel Rocha (58501)
 * Spellchecker - Group Project
 * EDA1 2023/2024
 * 
 * @file wMisspells.c
 * 
 * This file contains the implementation of the misspelling list structure and its functions.
 * The misspelling list structure is an hash table used to store the misspelled words.
 * Uses quadratic probing to solve collisions.
 * 
*************************************************************************************************************/


#include "spellChecker.h"


#define MIN_SIZE 13


/*************************************************************************************************************
 * 
 * This function creates a new list of misspells with the minimum length of 13.
 * It allocates memory for the list and for the entries array.
 * 
 * @param none
 * 
 * @return the new list of misspells
 * 
 ************************************************************************************************************/
misspellList* createTable() {
    misspellList* list = (misspellList*) malloc(sizeof(misspellList));
    if (list == NULL) {
        wprintf(L"Error creating misspellings list");
        return NULL;
    }

    list->size = MIN_SIZE;
    list->nElements = 0;
    list->entries = (misspell*) malloc(MIN_SIZE * sizeof(misspell));
    if (list->entries == NULL) {
        wprintf(L"Error creating misspellings list");
        free(list);
        return NULL;
    }

    for (int i = 0; i < MIN_SIZE; i++) {
        list->entries[i].key = NULL;
    }

    return list;
}


/*************************************************************************************************************
 * 
 * This function is used in case of collision.
 * It is used to calculate the position of a key in the list using the quadratic probing technique.
 * It calculates the hash value of the key using the djb2 algorithm.
 * 
 * @param str - the key for which we want to calculate the hash value
 * @param list - the list of misspells where we want to probe
 * 
 * @return the hash value of the key
 * 
 ************************************************************************************************************/
Index quadraticProbing(const entry *str, const misspellList* list) {
    Index hashVal = djb2(str, list->size);                                                          // Calculate the hash value of the key using the djb2 algorithm

    int i = 0;

    while (list->entries[(hashVal + i * i)%list->size].key != NULL) {
        i++;
        if (i == list->size) {                                                                      //after i reaches the size of the list, we will start probing previously probed positions
            return -1;
        }
    }
    
    return (hashVal + i * i)%list->size;
}

/*************************************************************************************************************
 * 
 * This function is not used in the current implementation.
 * It is used to calculate the position of a key in the list using the linear probing technique.
 * It calculates the hash value of the key using the djb2 algorithm.
 * 
 * @param str - the key for which we want to calculate the hash value
 * @param list - the list of misspells where we want to probe
 * 
 * @return the hash value of the key
 * 
 ************************************************************************************************************/
Index linearProbing (const entry* str, misspellList* list) {
    Index hashVal = djb2(str, list->size);                                                          // Calculate the hash value of the key

    int i = 0;

    while (list->entries[(hashVal + i)%list->size].key != NULL) 
        i++;
    
    return (hashVal + i)%list->size;
}


/*************************************************************************************************************
 * 
 * This function calculates the load factor of the list of misspells.
 * 
 * @param list - the list of misspells
 * 
 * @return the load factor of the list
 * 
************************************************************************************************************/
float loadFactor(misspellList* list) {
    return (float) list->nElements / list->size;
}


/*************************************************************************************************************
 * 
 * This function rehashes the list of misspells.
 * It creates a new list with a size of 2 times the size of the previous list.
 * It then inserts all the elements from the previous list into the new list.
 * Will destroy the old list.
 * 
 * @param list - the list of misspells that we want to rehash
 * 
 * @return the new list of misspells
 * 
 ************************************************************************************************************/
misspellList* rehash(misspellList* list) {
    unsigned long newSize = nextPrime(2*list->size);

    misspellList* newList = (misspellList*) malloc(sizeof(misspellList));
    if (newList == NULL) {
        wprintf(L"Error rehashing");
        return NULL;
    }

    // Initialize the new list
    newList->size = newSize;
    newList->nElements = 0;
    newList->entries = (misspell*) malloc(newSize * sizeof(misspell));
    if (newList->entries == NULL) {
        wprintf(L"Error rehashing");
        free(newList);
        return NULL;
    }

    for (unsigned long i = 0; i < newList->size; i++) {
        newList->entries[i].key = NULL;
    }

    // Insert all the elements from the old list into the new list
    for (unsigned long i = 0; i < list->size; i++) {
        if (list->entries[i].key != NULL) {
            newList=insertTable(newList, list->entries[i].key);
        }
    }

    // Destroy the old list
    list = destroyTable(list);

    return newList;

}


/*************************************************************************************************************************
 * 
 * This function inserts a key (in this case, a misspelled word) into the list.
 * It calculates the hash value of the key using the djb2 algorithm.
 * If there is a collision, it uses quadratic probing to find a new position.
 * 
 * @param list - the list of misspells where we want to insert the key
 * @param key - the key that we want to insert
 * 
 * @return the list of misspells after the key is inserted (or NULL if the misspelling could not be inserted)
 * 
 ************************************************************************************************************************/
misspellList* insertTable(misspellList* list, const entry* key) {
    if (list == NULL) {
        wprintf(L"Error: List of misspellings is not initialized");
        return NULL;
    }

    if (key == NULL) {
        wprintf(L"Error: Key is empty");
        return NULL;
    }

    int len = wcslen (key);

    // Calculate the hash value of the key

    Index i = djb2(key, list->size);

    if (list->entries[i].key != NULL) {
        i = quadraticProbing(key, list);
        if (i == -1) {
            list = rehash(list);
            i = quadraticProbing(key, list);
        }
    }

    // Insert the key in the list

    list->entries[i].key = (entry*) malloc((len+1) * sizeof(entry));
    if (list->entries[i].key == NULL) {
        wprintf(L"Error allocating memory for key");
        return NULL;
    }

    if (wcscpy(list->entries[i].key, key) == NULL) {
        wprintf(L"Error copying key");
        return NULL;
    }

    list->entries[i].aValue = asciiValue(key);

    list->nElements++;


    if (loadFactor(list) > 0.5) {                                                                   // Rehash if the load factor is greater than 0.5
        list = rehash(list);
    }

    return list;
}


/*************************************************************************************************************
 * 
 * This function searches for a key in the list.
 * It calculates the hash value of the key using the djb2 algorithm and quadratic probing.
 * The search is done by comparing the ASCII value of the key and only if the ASCII value is the same, the
 * wcscmp function is used to compare the keys.
 * 
 * @param list - the list of misspells where we want to search 
 * @param key - the key/misspelled word that we want to search for
 * 
 * @return true if the misspelling is found, false otherwise
 * 
 ************************************************************************************************************/
bool searchTable(const misspellList* list, const entry* key) {
    if (list == NULL) {
        wprintf(L"Error: List of misspellings is not initialized");
        return false;
    }
    
    if (key == NULL) {
        wprintf(L"Error: Key is empty");
        return false;
    }

    if (list->nElements == 0)                                                                       // If the list is empty, the key is not in the list
        return false;
    

    Index hash = djb2(key, list->size);
    unsigned long asciiVal = asciiValue(key);

    Index j = hash;
    for (unsigned long i = 0; list->entries[j].key != NULL ; i++) {                                 // Search for the key in the list
        if (list->entries[j].aValue == asciiVal) {                                                  // Compare the value of the key
            if (wcscmp(list->entries[j].key, key) == 0) {                                           // Compare the keys
                return true;
            }
        }
        j = (hash + i * i)%list->size;                                                              // Use quadratic probing to find the next position
    }

    return false;
}


/*************************************************************************************************************
 * 
 * This function destroys a misspelling list.
 * 
 * @param list - the list of misspells to be destroyed
 * 
 * @return NULL after the list is destroyed
 * 
 ************************************************************************************************************/
misspellList* destroyTable (misspellList* list) {
    if (list == NULL)
        return NULL;
    
    for (Index i = 0; i < list->size; i++){                                                         // Free the memory allocated for the keys
        if (list->entries[i].key != NULL) {
            free(list->entries[i].key);
            list->entries[i].key = NULL;
        }
    }

    free (list->entries);
    list->entries = NULL;
    list->size = 0;
    list->nElements = 0;

    free (list);
    list = NULL;

    return list;
}
