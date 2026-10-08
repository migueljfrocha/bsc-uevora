/*************************************************************************************************************
 * 
 * @authors: Miguel Pombeiro (57829) , Miguel Rocha (58501)
 * Spellchecker - Group Project
 * EDA1 2023/2024
 * 
 * @file auxiliary.c 
 * 
 * This file contains the implementation of the auxiliary functions used in the Spellchecker project.
 * 
*************************************************************************************************************/


#include "spellChecker.h"


/*************************************************************************************************************
* 
* This function reads the dictionary file and counts the number of new lines.
* The dictionary file consists on a list of words separated by new lines.
* Effectively, it counts the number of words in the dictionary.
* 
* @param file - the file to be read
* 
* @return the number of words in the dictionary
* 
*************************************************************************************************************/
unsigned int countWords(FILE* file) {
    char c;
    unsigned nWords = 1;
    
    while ((c = fgetc(file)) != EOF) {                                                              // read the file character by character
        if (c == '\n') {                                                                            // if the character is a new line, increment the number of words
            nWords++;
        }
    }

    return nWords;
}


/*************************************************************************************************************
 * 
 * This function calculates the next prime number after N.
 * If N is smaller than 10, it return a prime number larger than 10.
 * It makes use of the fact that a number is prime if it is not divisible by any number smaller than its square root.
 * 
 * @param N - the number after which we want to calculate the next prime number
 * 
 * @return the next prime number after N or if N is already prime, it returns N
 * 
*************************************************************************************************************/
unsigned long nextPrime( unsigned long N ) {
    unsigned long sqrtN=0, i=0;
    if (N<10) N=11;
    else if (N%2==0) N++;                                                                           // if N is even, increment it by 1

    while (1) {
        sqrtN=sqrt(N);                                                                              // calculate the square root of N
        for (i = 3;i<=sqrtN;i++) {                                                                  // check if N is divisible by any number smaller than its square root
            if (N%i==0)
                break;
        }
        if (i>sqrtN)                                                                                // if N is not divisible by any number smaller than its square root, it is prime
            return N;
        N+=2;
    }

   return 0;                                                                                        // for compilation purposes, it should never reach this point
}


/*************************************************************************************************************
 * 
 * This function calculates the previous prime number before N.
 * 
 * @param N - the number from which we want to calculate the previous prime number
 * 
 * @return the previous prime number before N
 *
*************************************************************************************************************/
unsigned long prevPrime (unsigned long N) {
    unsigned long i = 0;
    if (N % 2 == 0) N--;                                                                            // if N is even, decrement it by 1

    while (1) {
        N -= 2;
        for (i = 3; i <= sqrt(N); i++) {                                                            // check if N is divisible by any number smaller than its square root
            if (N % i == 0)
                break;
        }
        if (i > sqrt(N))                                                                            // if N is not divisible by any number smaller than its square root, it is prime
            return N;
    }

    return 0;                                                                                       // for compilation purposes, it should never reach this point  
}


/*************************************************************************************************************
 * 
 * This function calculates the ascii value or, in this case, the sum of the values of the characters in a string.
 * 
 * @param key - the string for which we want to calculate the ascii value
 * 
 * @return the ascii value of the string
 *
*************************************************************************************************************/
unsigned long asciiValue(const entry* key) {
    unsigned long sum = 0;
    int i = 0;

    while (key [i] != '\0') {                                                                       // iterate over the characters of the string and sum their values
        sum += key[i];
        i++;
    }

    return sum;
}


/*************************************************************************************************************
 * 
 * This function calculates the hash value of the key using the djb2 algorithm.
 * The djb2 algorithm is a simple hash function created by Daniel Bernstein.
 * Using a hash value of 5381, it iterates over the characters of the key and multiplies the hash value by 33 and adds the character.
 * hash(i) = hash(i-1) * 33 + str[i]
 * 
 * @param str - the key for which we want to calculate the hash value
 * @param size - the size of the dictionary
 * 
 * @return the hash value of the key
 *
*************************************************************************************************************/
Index djb2(const entry *str, const unsigned long size) {
    Index hash = 5381;                                                                              // initial hash value
    int c;

    while ((c = *str++))
        hash = ((hash << 5) + hash) + c;                                                            // hash * 33 + characater 

    return hash % size;
}


/*************************************************************************************************************
 * 
 * This function calculates another hash value for a string.
 * It uses the previous prime number before the size of the dictionary and the ascii value of the string.
 * hash2 = (hash - (asciiValue % hash)) % size
 * 
 * @param str - the key for which we want to calculate the hash value
 * 
 * @return the hash value of the key
 * 
*************************************************************************************************************/
Index hash2(const entry *str, const unsigned long size) {
    unsigned long hash = prevPrime(size);                                                           // previous prime number before the size of the dictionary
    unsigned long asciiVal = asciiValue(str);

    return (hash - (asciiVal % hash))%size;                                                         // R - (asciiValue % R) % size
}


/*************************************************************************************************************
 * 
 * This function helps generating suggestions for misspelled words.
 * It generates suggestions by inserting a single character in the word.
 * It checks if the new word is in the dictionary and if it is not already in the list of suggestions.
 * 
 * @param dict - the dictionary
 * @param list - the list of suggestions (pointer to a pointer of suggestionList so that rehash can be called)
 * @param word - the misspelled word
 * @param output - the file where the suggestions will be written
 * 
 * @return true if a suggestion was generated, false otherwise
 * 
*************************************************************************************************************/
bool characterInsert(const Dictionary* dict, suggestionList** list, const entry* word, FILE* output){
    wchar_t characters[] = L"abcdefghijklmnopqrstuvwxyzçáàâãéèêîíìóòõôúùûABCDEFGHIJKLMNOPQRSTUVWXYZÇÁÀÂÃÉÈÊÎÍÌÓÒÕÔÚÙÛ"; 
    int charLen = wcslen(characters);

    int len = wcslen(word);
    wchar_t* newWord = (wchar_t*) malloc((len+2) * sizeof(wchar_t));
    
    wchar_t pontuation = L' ';
    for(int i=0; i<len+1; i++){                                                                     // i -> position where the new character will be inserted
        for(int j=0; j<len+1; j++){                                                                 // j -> position in the new word; used to copy the characters from the original word
            if(j<i){
                newWord[j]=word[j];
            }
            else if(j>i){
                newWord[j]=word[j-1];                                                               // shift the characters to the right, one position
            }
        }
        newWord[len+1]=L'\0';

        for(int j=0; j<charLen; j++){                                                               // insert a character from the list in the new word
            newWord[i]=characters[j];
            if(searchEntry(dict, newWord) && !searchTable((*list), newWord)){                       // check if the new word is in the dictionary and if it is not already in the list of suggestions
                *list=insertTable((*list), newWord);
                fwprintf(output, L"%lc %ls", pontuation, newWord);
                pontuation = L',';
            }
        }
    }
    free(newWord);

    return pontuation == L','? true:false;                                                          //return true if a suggestion was generated, false otherwise
}


/*************************************************************************************************************
 * 
 * This function helps generating suggestions for misspelled words.
 * It generates suggestions by deleting a single character in the word.
 * It checks if the new word is in the dictionary and if it is not already in the list of suggestions.
 * 
 * @param dict - the dictionary
 * @param list - the list of suggestions (pointer to a pointer of suggestionList so that rehash can be called)
 * @param word - the misspelled word
 * @param output - the file where the suggestions will be written
 * @param control - a control variable that helps to determine the punctuation for the output
 * 
 * @return true if a suggestion was generated, false otherwise
 *
*************************************************************************************************************/
bool characterDelete(const Dictionary* dict, suggestionList** list, const entry* word, FILE* output, int control){

    int len = wcslen(word);
    wchar_t* newWord = (wchar_t*) malloc((len+1) * sizeof(wchar_t));
    wchar_t pontuation = control == 1? L',' : L' ';

    for(int i=0; i<len; i++){                                                                       // i -> position of the character to be deleted
        for(int j=0; j<len; j++){                                                                   // j -> position in the original word; used to copy the characters to the new word
            if(j<i){
                newWord[j]=word[j];
            }
            else if(j>i){
                newWord[j-1]=word[j];                                                               // shift the characters to the left, one position
            }
        }
        newWord[len-1]=L'\0';

        if(searchEntry(dict, newWord) && !searchTable((*list), newWord)){                           // check if the new word is in the dictionary and if it is not already in the list of suggestions
            *list=insertTable((*list), newWord);
            fwprintf(output, L"%lc %ls", pontuation, newWord);
            pontuation = L',';
        }
    }
    free(newWord);

    return pontuation == L','? true:false;                                                          //return true if a suggestion was generated, false otherwise
}


/*************************************************************************************************************
 * 
 * This function helps generating suggestions for misspelled words.
 * It generates suggestions by swapping two characters in the word.
 * It checks if the new word is in the dictionary and if it is not already in the list of suggestions.
 * 
 * @param dict - the dictionary
 * @param list - the list of suggestions (pointer to a pointer of suggestionList so that rehash can be called)
 * @param word - the misspelled word
 * @param output - the file where the suggestions will be written
 * @param control - a control variable that helps to determine the punctuation for the output
 * 
 * @return true if a suggestion was generated, false otherwise
 *
*************************************************************************************************************/
bool characterSwap(const Dictionary* dict, suggestionList** list, const entry* word, FILE* output, int control){
    
    int len = wcslen(word);
    wchar_t* newWord = (wchar_t*) malloc((len+1) * sizeof(wchar_t));
    wchar_t pontuation = control == 1? L',' : L' ';

    for(int i=0; i<len-1; i++) {                                                                    // i -> position of the first character to be swapped
        for(int j=0; j<len; j++) {                                                                  // j -> position in the original word; used to copy the characters to the new word and swap the desired character
            if(i==j){                                                                               // swap the characters
                newWord[j]=word[j+1];
                newWord[j+1]=word[j];
                j++;                                                                                // skip the swapped character
            }
            else{
                newWord[j]=word[j];
            }
        }
        newWord[len]=L'\0';

        if(searchEntry(dict, newWord) && !searchTable((*list), newWord)){                           // check if the new word is in the dictionary and if it is not already in the list of suggestions
            *list=insertTable((*list), newWord);
            fwprintf(output, L"%lc %ls", pontuation, newWord);
            pontuation = L',';
        }
    }
    free(newWord);
    return pontuation == L','? true:false;                                                          // return true if a suggestion was generated, false otherwise
}


/*************************************************************************************************************
 * 
 * This function helps generating suggestions for misspelled words.
 * It generates suggestions by replacing a single character in the word.
 * It checks if the new word is in the dictionary and if it is not already in the list of suggestions.
 * 
 * @param dict - the dictionary
 * @param list - the list of suggestions (pointer to a pointer of suggestionList so that rehash can be called)
 * @param word - the misspelled word
 * @param output - the file where the suggestions will be written
 * @param control - a control variable that helps to determine the punctuation for the output
 * 
 * @return true if a suggestion was generated, false otherwise
 *
*************************************************************************************************************/
bool characterReplace (const Dictionary* dict, suggestionList** list, const entry* word, FILE* output, int control) {

    wchar_t characters[] = L"abcdefghijklmnopqrstuvwxyzçáàâãéèêîíìóòõôúùûABCDEFGHIJKLMNOPQRSTUVWXYZÇÁÀÂÃÉÈÊÎÍÌÓÒÕÔÚÙÛ";    
    int charLen = wcslen(characters);

    int len = wcslen(word); 
    wchar_t* newWord = (wchar_t*) malloc((len+1) * sizeof(wchar_t));    
    wcscpy(newWord, word);

    wchar_t pontuation = control == 1? L',' : L' ';
    
    for (int i = 0; i<len; i++) {                                                                       // i -> position of the character to be replaced
        wchar_t temp = newWord[i];
        for (int j = 0; j < charLen; j++) {                                                             // j -> position in the list of characters; used to replace the character in the word
            newWord[i] = characters[j];

            if (newWord[i] != temp && searchEntry(dict, newWord) && !searchTable((*list), newWord)) {   // check if the new word is in the dictionary and if it is not already in the list of suggestions
                *list=insertTable((*list), newWord);
                fwprintf(output, L"%lc %ls", pontuation, newWord);
                pontuation = L',';
            }
        }
        newWord[i] = temp;                                                                              // reset the word to the original before checking the next character
    }

    free(newWord);
    return pontuation == L','? true:false;                                                              // return true if a suggestion was generated, false otherwise

}
