------------------------ MODULE step4_philo_ok_turn ------------------------

EXTENDS Integers, Sequences, TLC, FiniteSets
CONSTANTS NumPhilosophers
ASSUME NumPhilosophers > 0
NP == NumPhilosophers

(* --algorithm dining_philosophers

variables 
    forks = [fork \in 1..NP |-> 1],
    forks_alloc = [ p \in 1..NP |-> << p, (p + 1) % NP>> ],
    turn = NP - 1;
    
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
      l0: wait(turn);
      l1: wait(forks[self]);
      l2: wait(forks[((self + 1) % NP) + 1]); 
      Eat: print "eat";
      l3: signal(forks[self]);
      l4: signal(forks[((self + 1) % NP) + 1]);         
      l5: signal(turn);
    end either;
  end while;
end process;
end algorithm; *)


\* BEGIN TRANSLATION (chksum(pcal) = "cad2c8bd" /\ chksum(tla) = "8b14b135")
VARIABLES forks, forks_alloc, turn, pc

vars == << forks, forks_alloc, turn, pc >>

ProcSet == (1..NP)

Init == (* Global variables *)
        /\ forks = [fork \in 1..NP |-> 1]
        /\ forks_alloc = [ p \in 1..NP |-> << p, (p + 1) % NP>> ]
        /\ turn = NP - 1
        /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ \/ /\ pc' = [pc EXCEPT ![self] = "Think"]
                     \/ /\ pc' = [pc EXCEPT ![self] = "l0"]
                  /\ UNCHANGED << forks, forks_alloc, turn >>

Think(self) == /\ pc[self] = "Think"
               /\ PrintT("think")
               /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
               /\ UNCHANGED << forks, forks_alloc, turn >>

l0(self) == /\ pc[self] = "l0"
            /\ turn > 0
            /\ turn' = turn - 1
            /\ pc' = [pc EXCEPT ![self] = "l1"]
            /\ UNCHANGED << forks, forks_alloc >>

l1(self) == /\ pc[self] = "l1"
            /\ (forks[self]) > 0
            /\ forks' = [forks EXCEPT ![self] = (forks[self]) - 1]
            /\ pc' = [pc EXCEPT ![self] = "l2"]
            /\ UNCHANGED << forks_alloc, turn >>

l2(self) == /\ pc[self] = "l2"
            /\ (forks[((self + 1) % NP) + 1]) > 0
            /\ forks' = [forks EXCEPT ![((self + 1) % NP) + 1] = (forks[((self + 1) % NP) + 1]) - 1]
            /\ pc' = [pc EXCEPT ![self] = "Eat"]
            /\ UNCHANGED << forks_alloc, turn >>

Eat(self) == /\ pc[self] = "Eat"
             /\ PrintT("eat")
             /\ pc' = [pc EXCEPT ![self] = "l3"]
             /\ UNCHANGED << forks, forks_alloc, turn >>

l3(self) == /\ pc[self] = "l3"
            /\ forks' = [forks EXCEPT ![self] = (forks[self]) + 1]
            /\ pc' = [pc EXCEPT ![self] = "l4"]
            /\ UNCHANGED << forks_alloc, turn >>

l4(self) == /\ pc[self] = "l4"
            /\ forks' = [forks EXCEPT ![((self + 1) % NP) + 1] = (forks[((self + 1) % NP) + 1]) + 1]
            /\ pc' = [pc EXCEPT ![self] = "l5"]
            /\ UNCHANGED << forks_alloc, turn >>

l5(self) == /\ pc[self] = "l5"
            /\ turn' = turn + 1
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
            /\ UNCHANGED << forks, forks_alloc >>

philosopher(self) == MainLoop(self) \/ Think(self) \/ l0(self) \/ l1(self)
                        \/ l2(self) \/ Eat(self) \/ l3(self) \/ l4(self)
                        \/ l5(self)

Next == (\E self \in 1..NP: philosopher(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 
=============================================================================
\* Modification History
\* Last modified Sun Mar 28 15:30:48 CEST 2021 by aricci
\* Created Sun Mar 28 15:29:44 CEST 2021 by aricci
