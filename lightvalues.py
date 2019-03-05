import time
from networktables import NetworkTables
import threading
import math
import pathfinder as pf
from ctypes import WinDLL
import sys
import signal

# buttons[i][0] = name, buttons[i][1] = default value
buttons = [["Magic Button", False], ["Hatch Level 3", False], ["Hatch Level 1", False], ["Hatch Level 2", False],
         ["Cargo Level 1", False], ["Cargo Level 2", False], ["Cargo Level 3", False], ["Cargo Ship Cargo", False],
         ["Cargo Ship Hatch", False], ["Reset Button", False], ["SDS Out", False],
         ["SDS In", False], ["Ground Intake", False], ["Hatch Feeder", False], ["nil", False], ["nil", False]]

xvalues = [0.0, 0.0, 0.0, 0.0, 0.0]
zvalues = [0.0, 0.0, 0.0, 0.0, 0.0]
thetavalues = [0.0, 0.0, 0.0, 0.0, 0.0]

sampleSize = 5

seqNumber = 0

loopNum = 0

pacdrive = WinDLL("C:\\buttonbox\\PacDrive32.dll")
def signal_handler(signal, frame):
    pacdrive.PacShutdown()
    sys.exit(0)

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
ctable = NetworkTables.getTable('limelight')
pacdrive.PacInitialize()

table.putNumber("Trajectory Response", 0)
table.putNumber("Trajectory Request", 0)

signal.signal(signal.SIGINT, signal_handler)
signal.signal(signal.SIGTERM, signal_handler)

while True:
    # Button Box
    value=0

    if ctable.getNumber('tv', 0) == 1:
        table.putBoolean("Magic Button", True)
    else:
        table.putBoolean("Magic Button", False)
    for i, v in enumerate(buttons):
        if table.getBoolean(v[0], v[1]) == True:
            value |= (1 << i)
    
    pacdrive.PacSetLEDStates(0, value)

    # loopNum += 1
    # if loopNum % 10 == 0:
    #     tarpos = ctable.getNumberArray('camtran', [0,0,0,0,0,0])

    #     xvalues.append(tarpos[0])
    #     if len(xvalues) > sampleSize:
    #         xvalues.pop(0)

    #     if xvalues[-1] == 0.0:
    #         xvalues = []
    #         xavg = 0.0
    #     elif len(xvalues) == sampleSize:
    #         xavg = sum(xvalues)/len(xvalues)

    #     zvalues.append(tarpos[2])
    #     if len(zvalues) > sampleSize:
    #         zvalues.pop(0)

    #     if zvalues[-1] == 0.0:
    #         zvalues = []
    #         zavg = 0.0
    #     elif len(zvalues) == sampleSize:
    #         zavg = sum(zvalues)/len(zvalues)

    #     thetavalues.append(tarpos[4])
    #     if len(thetavalues) > sampleSize:
    #         thetavalues.pop(0)

    #     if thetavalues[-1] == 0.0:
    #         thetavalues = []
    #         thetaavg = 0.0
    #     elif len(thetavalues) == sampleSize:
    #         thetaavg = sum(thetavalues)/len(thetavalues)

    #     if zavg != 0.0:
    #         myangle = math.degrees(math.atan((xavg)/(-zavg)))
    #     else:
    #         myangle = 0.0
    #     print("z {}, x {}, theta {}, jersangle {}".format(zavg, xavg, thetaavg, myangle))
    #     #print(thetaavg - xavg)

    #     if len(thetavalues) == sampleSize and len(zvalues) == sampleSize and len(xvalues) == sampleSize:
    #         table.putBoolean("Magic Button", True)
    #     else:
    #         table.putBoolean("Magic Button", False)


    #print(tarpos)

    time.sleep(0.005)

    # TODO Find out when there is no target. Also don't run trajectory if there isn't. ("DONE")
    # TODO Generate Trajectory only on request. ("DONE")
    # TODO Keep a running average. ("DONE")
    # TODO Light magic button when ready. ("DONE")
    # TODO 100 dt


    if table.getNumber("Trajectory Request", -1) > seqNumber:
        seqNumber = table.getNumber("Trajectory Request", -1)
        print(thetaavg-myangle)
        points = [
            pf.Waypoint(0, 0, 0),
            pf.Waypoint(-zavg-15.0, xavg-4.0, -math.radians(thetaavg)), #math.radians(thetaavg)),   # Waypoint @ x=-4, y=-1, exit angle=-45 degrees                   # Waypoint @ x=-2, y=-2, exit angle=0 radians
                                                  # Waypoint @ x=0, y=0,   exit angle=0 radians
            #pf.Waypoint(30, 0, 0),
        ]

        info, trajectory = pf.generate(points, pf.FIT_HERMITE_CUBIC, pf.SAMPLES_HIGH,
                                    dt=0.005, # 50ms
                                    max_velocity=100.0,
                                    max_acceleration=100.0,
                                    max_jerk=100.0)
    
        pf.serialize_csv("triplecool.csv", trajectory)
        f = open("triplecool.csv", "r")
        table.putString("triplesee", f.read())
        table.putNumber("Trajectory Response", seqNumber)
        print(points[1])
        f.close()

    


PacShutdown(0)
sys.exit(0)