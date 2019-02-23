import subprocess
import time
from networktables import NetworkTables
import threading
import math
import pathfinder as pf

# buttons[i][0] = name, buttons[i][1] = default value
buttons = [["Reset Button", False], ["Hatch Level 3", False], ["Hatch Level 1", False], ["Hatch Level 2", False],
         ["Cargo Level 1", False], ["Cargo Level 2", False], ["Cargo Level 3", False], ["Cargo Ship Cargo", False],
         ["Cargo Ship Hatch", False], ["Magic Button", False], ["SDS In", False],
         ["SDS Out", False], ["Ground Intake", False], ["Hatch Feeder", False], ["nil", False], ["nil", False]]

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

btable = NetworkTables.getTable('SmartDashboard')
while True:
    value=0
    for i, v in enumerate(buttons):
        if btable.getBoolean(v[0], v[1]) == True:
            value |= (1 << i)
    
    flag = '{:04X}'.format(value)
    subprocess.Popen(["lightvalues.exe", flag], shell=True).wait()

ctable = NetworkTables.getTable('limelight')
while True:
    tarpos = ctable.getDouble('camtran', 0)
    x = tarpos[0]
    y = tarpos[1]
    z = tarpos[2]
    pitch = tarpos[3]
    theta = tarpos[4]
    roll = tarpos[5]

    print(tarpos)
    
    #points = [
    #    pf.Waypoint(z, x, math.radians(theta)),   # Waypoint @ x=-4, y=-1, exit angle=-45 degrees
    #    pf.Waypoint(-2, -2, 0),                     # Waypoint @ x=-2, y=-2, exit angle=0 radians
    #    pf.Waypoint(0, 0, 0),                       # Waypoint @ x=0, y=0,   exit angle=0 radians
    #]

    #info, trajectory = pf.generate(points, pf.FIT_HERMITE_CUBIC, pf.SAMPLES_HIGH,
    #                            dt=0.05, # 50ms
    #                            max_velocity=1.7,
    #                            max_acceleration=2.0,
    #                            max_jerk=60.0)
