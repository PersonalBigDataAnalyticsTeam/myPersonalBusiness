# -*- coding: utf-8 -*-
'''
Created on 2015-3-14

@author: solinari
'''



from copy import copy

class HMM(object):

    """ 
    Class for Hidden Markov Models 
    An HMM is a weighted FSA which consists of:

        - a set of states (0...C{self.states}) 
        - an output alphabet (C{self.alphabet}) 
        - a table of state transition probabilities (C{self.A}) 
        - a table of symbol emission probabilities (C{self.B})           
        - a list of initial probabilies (C{self.initial})

        We assume that the HMM is complete, and that all states are both initial and final 
        states.  
    """ 
    def __init__(self,states,alphabet,A,B,initial): 
        """ 
        Create a new FSA object 
        @param states: states 
        @type states: C{list} 
        @param alphabet: output alphabet 字母表
        @type finals: C{list} 
        @param A: transition probabilities 
        @type finals: C{list} of C{list}s 
        @param B: emission probabilities 
        @type finals: C{list} of C{list}s 
        @param initial: initial state probabilities 
        @type initial: C{list} of C{int}s

        @raise ValueError: the HMM is mis-specified somehow 
        """

        # basic configuration 
        self.states = states 
        self.N = len(self.states) 
        self.alphabet = alphabet

        # initial probabilities 
        self.initial = initial 
        if len(self.initial) != self.N: 
            raise ValueError,'only found %d initial probabilities'%len(self.initial) 
        if abs(sum(self.initial)-1.0) > 1e-8: 
            raise ValueError,'improper initial probabilities' 
        # transition probabilities 
        self.A = A 
        if len(self.A) != self.N: 
            raise ValueError,'only found %d transition probability distributions'%len(self.A) 
        for i in xrange(self.N): 
            if len(self.A[i]) != self.N: 
                raise ValueError,'only found %d transition probabilities for state %d'%(len(self.A[i]),i) 
            if abs(sum(self.A[i])-1.0) > 1e-8: 
                raise ValueError,'improper transition probabilities for state %d'%i

        # emission probabilities        
        self.B = B 
        if len(self.B) != self.N: 
            raise ValueError,'only found %d emission probability distributions'%len(self.B) 
        for i in xrange(self.N): 
            if i != self.initial:  # no output from initial state 
                if len(self.B[i]) != len(self.alphabet): 
                    raise ValueError,'only found %d emission probabilities for state %d'%(len(self.B[i]),i) 
                if i != abs(sum(self.B[i])-1.0) > 1e-8: 
                    raise ValueError,'improper emission probabilities for state %d'%i

    def allseqs(self,n): 
        """Generate all possible state sequences of length n"""

        if n == 0: 
            yield [ ] 
        else: 
            for first in self.states: 
                for rest in self.allseqs(n-1): 
                    yield [first] + rest

    def prob(self,S,O): 
        """Calculate P(O,S|M)"""

        assert len(O) == len(S) 
        assert len(O) > 0

        # convert state and output names to indices 
        try: 
            S = [self.states.index(t) for t in S] 
        except ValueError: 
            raise ValueError,'unknown state: %s'%t 
        try: 
            O = [self.alphabet.index(t) for t in O] 
        except ValueError: 
            raise ValueError,'unknown output: %s'%t

        prob = self.initial[S[0]] * self.B[S[0]][O[0]] 
        for t in xrange(1,len(S)): 
            prob *= self.A[S[t-1]][S[t]] * self.B[S[t]][O[t]] 
        return prob

    def probseq(self,O): 
        """Calculate P(O|M) the hard way"""

        # sum probs 
        prob = sum(self.prob(S,O) for S in self.allseqs(len(O)))

        return prob

    def forward(self,O): 
        """Calculate P(O|M) using the Forward Algorithm"""

        # convert output names to indices 
        try: 
            O = [self.alphabet.index(t) for t in O] 
        except ValueError: 
            raise ValueError,'unknown output: %s'%t

        # initialize trellis 
        trellis = [ [self.initial[t]*self.B[t][O[0]] for t in xrange(self.N)] ] 
        # fill in the rest of the trellis 
        for t in xrange(1,len(O)): 
            trellis.append([0.0] * self.N) 
            for s2 in xrange(self.N): 
                for s1 in xrange(self.N): 
                    trellis[-1][s2] += self.A[s1][s2] * self.B[s2][O[t]] * trellis[-2][s1] 
        # find total probability 
        return sum(trellis[-1])

def main():

    ## set up Eisner's HMM

    states = [ 'HOT', 'COLD' ] 
    output = [ 1, 2, 3] 
    transition = [ [ 0.7, 0.3 ], 
                   [ 0.4, 0.6 ] ]             
    emission = [ [ 0.2, 0.4, 0.4 ], 
                 [ 0.5, 0.4, 0.1 ] ] 
    initial = [ 0.8, 0.2 ]             
    m = HMM(states,output,transition,emission,initial)

    print 'P([HOT,HOT,COLD],[3,1,3]|M) =', 
    print m.prob(['HOT','HOT','COLD'],[3,1,3]) 
    #observation 3, 1, 3
#     print 'P([3,1,3]|M) =', 
#     print m.probseq([3,1,3]),'(by the naive method)'
    print 'P([3,1,3]|M) =', 
    print m.probseq([3,1,3]),'(by the naive method)'
    
    print 'P([3,1,3]|M) =', 
    print m.forward([3,1,3]),'(by the Forward Algorithm)'

main()