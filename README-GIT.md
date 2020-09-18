# 「Git」合并多个 Commit

## 提交时候,合并后一笔提交到前一笔提交上的命令：
    git log --oneline
    git add 某个文件
    git commit --fixup=要合并到的commintId
    git log --oneline
    git rebase -i --autosquash 要合并到的commintId前一个commitId
    git push -f origin master

## 合并已经提交到库上的commintId
    git log --oneline
    //[startpoint] [endpoint]则指定了一个编辑区间，如果不指定[endpoint]，则该区间的终点默认是当前分支HEAD所指向的commit(注：该区间指定的是一个前开后闭的区间)。
    git rebase -i  [startpoint]  [endpoint]
    或者 
    git rebase -i HEAD~3 
    //进入编辑模式
    pick 的意思是要会执行这个 commit
    squash：将该commit和前一个commit合并（缩写:s）
    fixup：将该commit和前一个commit合并，但我不要保留该提交的注释信息（缩写:f）
    将 需要和并到上一个提交的 commit 前方的命令改成 squash 或 s，然后输入:wq以保存并退出
    会看到 commit message 的编辑界面，重新整理下Commit 的msg 保存退出
    git push -f origin master

