"""
	Det beste python-programmet som nokonsinne er skrivi!!!!!

	Sender inn ei oppdatering til serveren, vha. telnet.
"""
import subprocess, datetime, time

process = subprocess.Popen(['telnet', '0.0.0.0', '4104'], stdout=subprocess.PIPE, stdin=subprocess.PIPE, stderr=subprocess.STDOUT)

ID = "11"
x = "0.0"
y = "0.5"
puls = "100"
temp = "39.5"
alarm = "0"

tid = str(time.mktime(datetime.datetime.now().timetuple()))
tid = tid.split(".")[0]

utstring = "U@"+ID+"@"+x+"@"+y+"@"+puls+"@"+temp+"@"+alarm+"@"+tid

print utstring

utput = process.communicate(input=utstring)

print utput
