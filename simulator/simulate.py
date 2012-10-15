"""
	Det beste python-programmet som nokonsinne er skrivi!!!!!

	Sender inn ei oppdatering til serveren, vha. telnet.
"""
import subprocess, datetime, time

process = subprocess.Popen(['telnet', '0.0.0.0', '4104'], stdout=subprocess.PIPE, stdin=subprocess.PIPE, stderr=subprocess.STDOUT)

ID = "16"
x = "65.0"
y = "62.5"
puls = "99"
temp = "37.5"
alarm = "0"

tid = str(time.mktime(datetime.datetime.now().timetuple()))
tid = tid.split(".")[0]

utstring = "U@"+ID+"@"+x+"@"+y+"@"+puls+"@"+temp+"@"+alarm+"@"+tid

print utstring

utput = process.communicate(input=utstring)

print utput
