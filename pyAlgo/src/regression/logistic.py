#coding=utf-8
'''
Created on 2015-3-11

@author: solinari
'''
import numpy
import matplotlib.pyplot as plt #添加matplotlib模块

class Logistic(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
    #加载数据,返回数据和类标签
    #strip()去除空白字符包括' ','\t','\n'等，split()分割得到数据
    #例如：
    #>>> s = '  sda  daw dw vce '
    #>>> s.strip().split()
    #['sda', 'daw', 'dw', 'vce']
    def loadDataSet(self):
        dataMat = []; labelMat = []
        fr = open('testSet.txt')
        for line in fr.readlines():
            lineArr = line.strip().split()
            dataMat.append([1.0, float(lineArr[0]), float(lineArr[1])]) #得到数据列表  (x,y)一组
            labelMat.append(int(lineArr[2])) #类标签 
#             labelMat.append(0)
        return dataMat,labelMat
    
    def sigmoid(self, inX):
        return 1.0/(1+numpy.exp(-inX))
    
    
    #梯度下降法，weights是每个对应的dataMatrix值的权重（回归系数）
    #计算全部的dataMatrix数据，然后对weights进行更新，计算量大，但是结果较准确
    def gradAscent(self, dataMatIn, classLabels):
        dataMatrix = numpy.mat(dataMatIn)             #数据列表转换成矩阵
        labelMat = numpy.mat(classLabels).transpose() #类标签列表转换成矩阵
        m,n = numpy.shape(dataMatrix)#得到dataMatrix矩阵大小
        alpha = 0.001 #每次上升的步长
        maxCycles = 500 #迭代次数
        weights = numpy.ones((n,1))
        for k in range(maxCycles):              
            h = self.sigmoid(dataMatrix*weights)   #计算假设函数h（列向量）  
            error = (labelMat - h)            #类标签和假设函数的误差
            weights = weights + alpha * dataMatrix.transpose()* error#对weights进行迭代更新
        return weights
    
    def plotBestFit(self, weights):
        dataMat,labelMat=self.loadDataSet()
        dataArr = numpy.array(dataMat)
        n = numpy.shape(dataArr)[0] #n=100
        xcord1 = []; ycord1 = []
        xcord2 = []; ycord2 = []
        for i in range(n):
            if int(labelMat[i])== 1: #如果i属于1类，则记录x，y的坐标
                xcord1.append(dataArr[i,1]); ycord1.append(dataArr[i,2])
            else: #如果i属于0类，则记录x，y的坐标
                xcord2.append(dataArr[i,1]); ycord2.append(dataArr[i,2])
        fig = plt.figure()
        ax = fig.add_subplot(111)
        ax.scatter(xcord1, ycord1, s=30, c='red', marker='s')
        ax.scatter(xcord2, ycord2, s=30, c='green')
        x = numpy.arange(-3.0, 3.0, 0.1) #x的范围
        #设定直线为W0X0+W1X1+W2X2=0
        y = (-weights[0]-weights[1]*x)/weights[2]
        ax.plot(x, y)
        plt.xlabel('X1'); plt.ylabel('X2');
        plt.show()

logistic = Logistic()
data,label = logistic.loadDataSet()
weights = logistic.gradAscent(data,label)
print weights
logistic.plotBestFit(weights.getA())  