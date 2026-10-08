void drawBoard (int *pBoard, int player, int mode);

int playerRotate (int player);

int checkWin (int *pBoard);

int askNextMove (int *pBoard, int player, int mode);

int checkIfValid (int *pBoard, int player, int nextMove, int mode);

int theMove (int *pBoard, int nextMove);

void capture (int *pBoard, int player, int landed);

int checkNRocks (int *pBoard);

int whenNoRocks (int *pBoard);

void allRocksToDeposit (int *pBoard);

int checkForEnd (int *pBoard, int *player);

void saveFile (int *pBoard);

int computerMove(int *pBoard, int player, int mode);

void loadGame(int *pBoard, int argc, char *argv[]);

void printWinner(int *pBoard, int mode, int win);