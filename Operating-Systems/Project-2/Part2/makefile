CC = gcc

SRCS = main.c scheduler.c process.c queue.c list.c auxiliary.c memory_management.c
OBJS = $(SRCS:.c=.o)

TARGET = main

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) -o $(TARGET) $(OBJS)

%.o: %.c
	$(CC) -c $< -o $@

run: $(TARGET)
	./$(TARGET) $(ARG) > Output/output2T$(ARG).out 

clean:
	rm -f $(OBJS) $(TARGET)