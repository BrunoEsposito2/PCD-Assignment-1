---------------------------- MODULE step4_philo ----------------------------

EXTENDS Integers, Sequences, TLC, FiniteSets
CONSTANTS NumPhilosophers
ASSUME NumPhilosophers > 0
NP == NumPhilosophers

(* --algorithm dining_philosophers

variables 
    forks = [fork \in 1..NP |-> 1 ];
    
macro wait(s) begin
  await s > 0;  
  s := s - 1;
end macro;

macro signal(s) begin
  s := s + 1;
end macro;

process philosopher \in 1..NP
begin MainLoop:
  while TRUE do
    either
      Think: print "think";
    or
      l1: wait(forks[self]);
      l2: wait(forks[((self + 1) % NP) + 1]); 
      Eat: print "eat";
      l3: signal(forks[self]);
      l4: signal(forks[(((self + 1) % NP) + 1)]);         
    end either;
  end while;
end process;
end algorithm; *)


\* BEGIN TRANSLATION (chksum(pcal) = "56e2f36" /\ chksum(tla) = "7dd78fd2")
VARIABLES forks, pc

vars == << forks, pc >>

ProcSet == (1..NP)

Init == (* Global variables *)
        /\ forks = [fork \in 1..NP |-> 1 ]
        /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ \/ /\ pc' = [pc EXCEPT ![self] = "Think"]
                     \/ /\ pc' = [pc EXCEPT ![self] = "l1"]
                  /\ forks' = forks

Think(self) == /\ pc[self] = "Think"
               /\ PrintT("think")
               /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
               /\ forks' = forks

l1(self) == /\ pc[self] = "l1"
            /\ (forks[self]) > 0
            /\ forks' = [forks EXCEPT ![self] = (forks[self]) - 1]
            /\ pc' = [pc EXCEPT ![self] = "l2"]

l2(self) == /\ pc[self] = "l2"
            /\ (forks[((self + 1) % NP) + 1]) > 0
            /\ forks' = [forks EXCEPT ![((self + 1) % NP) + 1] = (forks[((self + 1) % NP) + 1]) - 1]
            /\ pc' = [pc EXCEPT ![self] = "Eat"]

Eat(self) == /\ pc[self] = "Eat"
             /\ PrintT("eat")
             /\ pc' = [pc EXCEPT ![self] = "l3"]
             /\ forks' = forks

l3(self) == /\ pc[self] = "l3"
            /\ forks' = [forks EXCEPT ![self] = (forks[self]) + 1]
            /\ pc' = [pc EXCEPT ![self] = "l4"]

l4(self) == /\ pc[self] = "l4"
            /\ forks' = [forks EXCEPT ![(((self + 1) % NP) + 1)] = (forks[(((self + 1) % NP) + 1)]) + 1]
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]

philosopher(self) == MainLoop(self) \/ Think(self) \/ l1(self) \/ l2(self)
                        \/ Eat(self) \/ l3(self) \/ l4(self)

Next == (\E self \in 1..NP: philosopher(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 
=============================================================================
\* Modification History
\* Last modified Mon Mar 29 16:40:19 CEST 2021 by aricci
\* Created Sun Mar 28 15:25:23 CEST 2021 by aricci
