import subprocess
import time
from networktables import NetworkTables
import threading

# buttons[i][0] = name, buttons[i][1] = default value
buttons = [["Reset Button", False], ["Hatch Level 3", False], ["Hatch Level 1", False], ["Hatch Level 2", False],
         ["Cargo Level 1", False], ["Cargo Level 2", False], ["Cargo Level 3", False], ["Cargo Ship Cargo", False],
         ["Cargo Ship Hatch", False], ["Magic Button", False], ["SDS In", False],
         ["SDS Out", False], ["Hatch Out", False], ["Hatch In", False], ["nil", False], ["nil", False]]

#NetworkTables
cond = threading.Condition()
notified = [False]

def connectionListener(connected, info):
    print(info, '; Connect= %s' % connected)
    with cond:
        notified[0] = True
        cond.notify()

NetworkTables.initialize(server='roboRIO-2823-FRC.local')
NetworkTables.addConnectionListener(connectionListener, immediateNotify=True)

with cond:
    print("Waiting")
    if not notified[0]:
        cond.wait()

print("Connected!")

table = NetworkTables.getTable('SmartDashboard')
while True:
    value=0
    for i, v in enumerate(buttons):
        if table.getBoolean(v[0], v[1]) == True:
            value |= (1 << i)
    
    flag = '{:04X}'.format(value)
    subprocess.Popen(["lightvalues.exe", flag], shell=True).wait()


