# file-dir-search
A high performance file/dir search utility program.

This program that takes command line arguments and searches the required file in the current folder and the sub folders . Search could be for files or folders or both . Below are the four parameters that would be passed .

1) A top folder name : Folder from where search would begin. (Ex : C:/MyFolder ) 
2) Search Option (File /Folder /Both ) 
3) Search Pattern : A regular expression : (*my*.txt ) 
4) Timeout in seconds : once the operation treaches this timeout search should be stopped saying " Could not complete operation " and results obtained till then must be returned .
