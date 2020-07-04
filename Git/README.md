# Git

## Git이란?

- 프로그램 등의 소스 코드 관리를 위한 분산 버전 관리 시스템



## Git 명령어

### git init

- create a new, empty repository in the current directory

### git clone <path-to-repository-to-clone>

- create an identical copy of an existing repository

### git status

- display the current status of the repository

### git log

- display all of the commits of a repository

### git log --oneline

- lists one commit per line
- shows the first 7 characters of the commit's SHA
- shows the commit's message

### git log --stat

- displays the file(s) that have been modified
- displays the number of lines that have been added/removed
- displays a summary line with the total number of modified files and lines that have been added/removed

### git log -p (<SHA-of-a-commit>) (git log --patch)

- displays the files that have been modified
- displays the location of the lines that have been added/removed
- displays the actual changes that have been made

### git log --oneline --decorate --graph --all

- shows all branches and therefore all commits in the repository

### git add <file1> <file2> … <fileN>

- move files from the Working Directory to the Staging Index

### git commit

- takes files from the Staging Index and saves them in the repository

### git diff

- see changes that have been made but haven't been committed, yet

### git tag -a <tag-name>

- add a marker on a specific commit

### git tag -d <tag-name> (git tag --delete)

- delete a marker on a specific commit

### git branch <branch-name> <SHA-of-a-commit>

- list all branch names in the repository
- create new branches
- delete branches (-d option)

### git checkout <new-branch-name> <branch-name>

- remove all files and directories from the Working Directory that Git is tracking
(files that Git tracks are stored in the repository, so nothing is lost)
- go into the repository and pull out all of the files and directories of the commit that the branch points to

### git merge <name-of-branch-to-merge-in>

- combine branches in Git

- Merge type
  - Fast-forward merge 
    - The branch being merged in must be ahead of the checked out branch. 
    - The checked out branch's pointer will just be moved forward to point to the same commit as the other branch.
  - The regular type of merge
    - two divergent branches are combined
    - a merge commit is created

- Merge conflict
  - locate and remove all lines with merge conflict indicators
  - determine what to keep
  - save the file(s)
  - stage the file(s)
  - make a commit

### git commit --amend

- alter the most-recent commit
  - edit the file(s)
  - save the file(s)
  - stage the file(s)
  - and run git commit --amend

### git revert <SHA-of-commit-to-revert>

- reverse a previously made commit

### git reset <reference-to-commit>

- move the HEAD and current branch pointer to the referenced commit
- erase commits (--hard)
- moves committed changes to the staging index (--soft)
- unstages committed changes (--mixed)

### Ancestry references

- ^ : indicates the parent commit
- ~ : indicates the first parent commit