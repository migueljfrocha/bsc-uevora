/*************************************************************************************************************
 * 
 * @authors: Miguel Pombeiro (57829) , Miguel Rocha (58501)
 * Spellchecker - Group Project
 * EDA1 2023/2024
 * 
 * @file spell.c
 * 
 * This file contains the implementation and main logic of the spellchecker.
 * The spellchecker reads a dictionary file and a text file with misspelled words.
 * It then checks the misspelled words against the dictionary and outputs suggestions of correct words.
 * 
+*************************************************************************************************************/


#include <locale.h>
#include <wctype.h>

#include "spellChecker.h"


#define COLOR_BOLD L"\033[1m"
#define COLOR_RESET L"\033[0m"


/*************************************************************************************************************
 * 
 * This function reads a dictionary file and creates a dictionary with the words in the file.
 * It counts the number of words in the file and creates a dictionary with a size of the next prime number after 2*nWords.
 * It then inserts the words in the dictionary.
 * 
 * @param fileDic - the string containing the name of the dictionary file
 * 
 * @return the dictionary
 * 
*************************************************************************************************************/
Dictionary* lerDicionario (const char* fileDic) {

    unsigned long prime = 0;
    unsigned int nWords = 0;
    Dictionary* dict = NULL;
    wchar_t word[100] = {0};

    FILE* file = fopen(fileDic, "r"), *file1;                                                       // Open the dictionary file
    if (file == NULL) {
        wprintf(L"Error: File not found\n");
        return NULL;
    }

    nWords = countWords(file);                                                                      // Count the number of words in the file

    fclose (file);

    file1 = fopen(fileDic, "r");                                                                    // Open the dictionary file again because the fseek/rewind do not work

    prime = nextPrime(2*nWords);

    dict = createDictionary(prime);

    int i = 0;

    while (fwscanf(file1, L"%99ls", word) != EOF) {                                                 // Read the words up to 99 characters from the file until the end of the file
        if (insertEntry(dict, word) == false) {                                                     // Insert the word in the dictionary
            wprintf(L"Error inserting entry\n");
            break;
        }
    }

    if (!feof(file1)) {                                                                             // Check if the end of the file was reached, if not, the dictionary was not created correctly
        wprintf(L"Error creating dictionary");
        fclose(file1);
        dict = destroyDictionary(dict);
        return NULL;
    }

    fclose(file1);

    wprintf(L"\n" COLOR_BOLD L"Dictionary Created" COLOR_RESET L"\n");

    wprintf(L"Dictionary Size: %lu\n", dict->size);
    wprintf (L"Number of Entries: %lu\n", dict->nElements);
    printCollisions();

    return dict;
}


/*************************************************************************************************************
 * 
 * This function reads a text file with misspelled words and outputs a file with suggestions of correct words.
 * It reads the file line by line and parses the words.
 * It then checks if the word is in the dictionary.
 * If it is not, it checks if the word is in the list of misspellings.
 * If it is not, it inserts the word in the list.
 * 
 * @param fileTex - the name of the text file to be checked
 * @param fileOut - the name of the output file
 * @param dict - the dictionary
 * 
 * @return the list of misspellings
 * 
*************************************************************************************************************/
misspellList* spellCheck (const char* fileTex, const char* fileOut, const Dictionary* dict) {
    
    misspellList* list = NULL;
    wchar_t* line = NULL , *parser = NULL;
    unsigned long bufferSize = 256;
    entry* word = NULL;
    unsigned long nWordsChecked = 0;
    const wchar_t* delimiters = L" \n\t.,;:!?-_()[]{}<>+-*/=\"'0123456789";

    FILE *misspFile, *output;
    
    misspFile = fopen (fileTex, "r");                                                               // Open the file to check
    if (misspFile == NULL) {
        wprintf(L"Error: File to spellcheck not found\n");
        return NULL;
    }

    output = fopen (fileOut, "w");                                                                  // Open the output file
    if (output == NULL) {
        wprintf(L"Error: Could not open the output file\n");
        return NULL;
    }

    fwprintf (output, L"################################################################\n#####################                      #####################\n#####################     SPELLCHECKER     #####################\n#####################                      #####################\n################################################################\n\n\n");
    fwprintf (output, L"Checking misspells from file : \"%s\"\n\nKey:\n - \"Misspelled Word\"  ----->  Suggestions\n\n\n", (wchar_t*) fileOut );

    list = createTable();

    line = (wchar_t*) malloc (bufferSize * sizeof(wchar_t));
    if (line == NULL) {
        wprintf(L"Error allocating memory for line");
        fclose(misspFile);
        fclose(output);
        return NULL;
    }

    while (fgetws(line, bufferSize, misspFile) != NULL) {                                           // Read the file 256 characters at a time until the end of the file 
        word = (entry*) wcstok (line, delimiters, &parser);                                         // Parse the words from the line (stores the first word in word and the rest in parser)
        while (word != NULL) {
            nWordsChecked++;
            if (iswupper(word[0]) && searchEntry(dict, word)) {                                     // Check if the word starts with uppercase and is in the dictionary (proper noun/beginning of sentence)
                    word = (entry*) wcstok (NULL, delimiters, &parser);                             //      Does not accept any other instances of uppercase words
                    continue;
            }

            wchar_t* lower = (entry*) malloc ((wcslen(word)+1)*sizeof(wchar_t));
            wcscpy (lower, word);
            for (int i = 0; word[i] != L'\0'; i++) {                                                // Convert the word to lowercase
                lower[i]=towlower(word[i]);
            }

            if (searchEntry(dict, lower) == false){                                                 // Check if the word is in the dictionary
                if (searchTable(list, lower) == false){                                             // Check if the word is in the list of misspellings
                    
                    if ((list=insertTable(list, word)) == NULL) {                                   // Insert the word in the list of misspellings
                        wprintf(L"Error inserting misspelling\n");
                        list = destroyTable(list);
                        return NULL;
                    }

                    fwprintf(output, L" - %ls  ----->", word); 

                    if (geraOutput(lower, dict, output) == false) {                                 // Generate suggestions for the misspelled word
                        wprintf(L"Error generating output\n");
                        list = destroyTable(list);
                        return NULL;
                    }
                }
            }
            word = (entry*) wcstok (NULL, delimiters, &parser);                                     // Parse the next word
        }
    }

    if (line) {
        free (line);
        line = NULL;
    }

    if (!feof(misspFile)) {
        wprintf(L"Error reading file");
        fclose(misspFile);
        list = destroyTable(list);
        return NULL;
    }

    fwprintf (output, L"\n\n\nSummary:\n - Number of Words Checked : %lu\n - Misspelled Words Found : %lu", nWordsChecked, list->nElements);

    fclose (misspFile);
    fclose (output);

    wprintf(L"\n" COLOR_BOLD L"List of Misspelled Words Created" COLOR_RESET L"\n");

    wprintf(L"List Size: %lu\n", list->size);
    wprintf(L"Number of Entries: %lu\n", list->nElements);
    wprintf(L"Loading Factor: %f\n", (float)list->nElements/(float)list->size);

    return list;

}


/**+**********************************************************************************************************
 * 
 * This function generates suggestions for a misspelled word and outputs them to a file.
 * It generates suggestions by inserting, deleting, swapping and replacing characters in the word.
 * It then checks if the suggestions are in the dictionary, inserts them in a hash table and outputs them to a file.
 * If no suggestions are found, it outputs "no suggestions".
 * 
 * @param word - the misspelled word
 * @param dict - the dictionary
 * @param output - the output file
 * 
 * @return true if the suggestions are generated and outputted successfully, false otherwise
 * 
*************************************************************************************************************/
bool geraOutput (const entry* word, const Dictionary* dict, FILE* output) {
    
    int control = 0;

    suggestionList *list = createTable();                                                           // Create a list of suggestions
    if (list == NULL) {
        wprintf(L"Error creating suggestions list\n");
        return false;
    }

    if (characterInsert(dict, &list, word, output))                                                 // Generate suggestions by inserting characters
        control = 1;

    if (wcslen(word) > 1) {
        if (characterDelete(dict, &list,  word, output, control))                                   // Generate suggestions by deleting characters
            control = 1;
        if (characterSwap(dict, &list, word, output, control))                                      // Generate suggestions by swapping characters
            control = 1;
        if (characterReplace(dict, &list, word, output, control))                                   // Generate suggestions by replacing characters
            control = 1;
    }

    list = destroyTable(list);

    control == 1? fwprintf(output, L"\n") : fwprintf(output, L"  no suggestions\n");

    return true;
}


/*************************************************************************************************************
 * 
 * Main function
 * This function contains the main logic of the spellchecker program.
 * 
 * @param void - has no parameters
 * 
 * @return EXIT_SUCCESS if the program runs successfully, EXIT_FAILURE otherwise
 *
*************************************************************************************************************/
int main (void) {

    setlocale(LC_ALL, "");                                                                          // Set the locale to the system's default to support wide characters and ensure compatibility

    char *fileDic = "portuguese.txt";                                                               // Dictionary file
    char *fileTex = "erros.txt";                                                                    // Text file with misspelled words
    char *fileOut = "output.txt";                                                                   // Output file
    
    Dictionary* dict = NULL;
    misspellList* list = NULL;

    if ((dict = lerDicionario(fileDic)) == NULL) {                                                  // Create the dictionary
        wprintf(L"Error creating dictionary\n");
        return EXIT_FAILURE;
    }

    if ((list = spellCheck(fileTex, fileOut, dict)) == NULL) {                                      // Create the list of misspellings
        wprintf(L"Error creating list of misspellings\n");
        return EXIT_FAILURE;
    }

    list = destroyTable(list);
    dict = destroyDictionary(dict);

    wprintf(L"\n" COLOR_BOLD L"List of misspelled words and suggestions created successfully: \"%s\"" COLOR_RESET L"\n", (wchar_t*) fileOut);

    return EXIT_SUCCESS;
}