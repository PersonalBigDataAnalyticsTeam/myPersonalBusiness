'''
Created on 2015-3-27

@author: solinari
'''

from numpy import *
from matplotlib import *
from matplotlib.pyplot import *

GREEN = "green"
RED = "RED"
DRAWOFFSET = -0.2
WIDTH = 0.4

class PlotGraph(object):
    '''
    classdocs
    '''

    def __init__(self, fileName):
        '''
        Constructor
        '''

        fileHandle = open ( 'PriceStage.dat', 'r' )
        line = fileHandle.readline()
        while (line):
            self.day = 0
            line = line[0:len(line)-2]
            days = line.split(";")
            for day in days:
                infos = day.split(" ")
                self.setPrice(float(infos[0]), float(infos[1]), float(infos[2]), float(infos[3]))
                self.setVolume(float(infos[4]))

                self.plotOneDay()
                self.day += 1
#                 print infos
            line = fileHandle.readline()
            show()
    #plotGraph
#         self.plotOneDay()
#         show()
        fileHandle.close()
    
    def setPrice(self, priceStart, price, priceHigh, priceLow):
        self.priceStart = priceStart
        self.price = price
        self.priceHigh = priceHigh
        self.priceLow = priceLow
    
    def setColor(self, priceStart, price):
        if priceStart > price:
            return GREEN
        else:
            return RED
        
    def setVolume(self, volume):
        self.volume = volume/1000000
    
    def oneDay(self):
        pass
        #setPrice
    #setVolume
        
    def plotOneDay(self):
        pass
        #setOneDay
#         self.setVolume(10000)
#         self.setPrice(8.5, 9.5, 10.5, 7.5)
        color = self.setColor(self.priceStart, self.price)
        priceHeight = abs(self.price - self.priceStart)
        if (self.price > self.priceStart):
            priceBottom = self.priceStart
        else:
            priceBottom = self.price
            
        bar(self.day+DRAWOFFSET, height = self.volume, width = WIDTH, facecolor=color, edgecolor=color)
        plot((self.day, self.day),(self.priceHigh,self.priceLow), color = color)
        bar(self.day+DRAWOFFSET, height = priceHeight, bottom = priceBottom, width = WIDTH, facecolor=color, edgecolor=color)
        
        
        
        
        
        
#         self.day = 1
#         self.setVolume(13000)
#         self.setPrice(9.5, 9.0, 10.0, 8.7)
#         color = self.setColor(self.priceStart, self.price)
#         priceHeight = abs(self.price - self.priceStart)
#         if (self.price > self.priceStart):
#             priceBottom = self.priceStart
#         else:
#             priceBottom = self.price
            
        bar(self.day+DRAWOFFSET, height = self.volume, width = WIDTH, facecolor=color, edgecolor=color)
        plot((self.day, self.day),(self.priceHigh,self.priceLow), color = color)
        bar(self.day+DRAWOFFSET, height = priceHeight, bottom = priceBottom, width = WIDTH, facecolor=color, edgecolor=color)
        
        
        
        
        
        
#         bar(left = 2 ,height = 3.5, facecolor=GREEN, edgecolor=GREEN)
#         bar(left = 100, height = 10)
        axis([-1,20,0,50])


PlotGraph("")