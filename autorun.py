from subprocess import Popen, PIPE

tests = {"TestA" : "outA.txt", 
         "TestB" : "outB.txt",
         "TestC" : "outC.txt",
         "TestD" : "outD.txt", 
         "TestE" : "outE.txt",
         "TestF" : "outF.txt",
         "TestG" : "outG.txt",
         "TestH" : "outH.txt"}

for key, value in tests.items() :
    cmd = ['java', '-jar', 'keyConstructors.jar', 'ist.meic.pa.test.' + key]
    p = Popen(cmd, stdout=PIPE, stderr=PIPE)
    stdout, stderr = p.communicate()    
    data = str(stderr,'utf-8')
    data = data.replace("\r\n", "\n")
    file = open("Tests/" + value, 'r')    
    if file.read() == data :
        print(key  + " OK!")
    else :
        print(key + " Failed!")
    file.close()