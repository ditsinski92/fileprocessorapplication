
The file is split into 100 equal-size chunks based on its byte length.

Each chunk is processed remotely by a compute node (using RemoteExecutor).

Chunk processing skips partial lines to avoid duplication.

Each node computes the max word count in its chunk.

The Tasklet collects all results and determines the global maximum.
