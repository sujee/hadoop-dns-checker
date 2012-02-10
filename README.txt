Hadoop is picky about DNS (and reverse DNS!)
Every node in the cluster has to have DNS working properly.

This script checks DNS and reverse DNS for hosts given.  Hosts are specified in a file, on a line by line basis.

What is needed:
    - Java (JDK 1.6) : which you will have installed in a Hadoop cluster anyway
    - rsync (to distribute files to remote machines)
    - SSH (and password less SSH setup between hosts)

Compile the source files first:
    ./compile.sh
this will create a file 'a.jar'.  This jar will be used by run scripts

the script 'run.sh'  runs the checks on the current host:
    ./run.sh   hosts_file

'run-on-cluster.sh' runs the tests on all machines specified in hosts file
    - it uses SSH to login to remote hosts and run the tests.  So it is handy to have password-less SSH configured.  
      In a typical Hadoop cluster, 'master' host is setup to have password-less SSH to all slaves.  So this script can be run from master server

    - it logs into each host and runs the DNS test

    ./run-on-cluster.sh  hosts_file

Happy Hadooping!

Sujee Maniyam
s@sujee.net
