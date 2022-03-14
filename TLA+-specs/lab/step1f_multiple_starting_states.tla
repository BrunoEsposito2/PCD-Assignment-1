------------------ MODULE step1f_multiple_starting_states ------------------

EXTENDS TLC, Integers, Sequences

(*--algorithm test
    
process p \in 1..2
variable x \in 1..3
begin
  l1: print self;
  l2: print x;
end process;

end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "43192de" /\ chksum(tla) = "856e7586")
VARIABLES pc, x

vars == << pc, x >>

ProcSet == (1..3)

Init == (* Process p *)
        /\ x \in [1..3 -> 1..4]
        /\ pc = [self \in ProcSet |-> "l1"]

l1(self) == /\ pc[self] = "l1"
            /\ PrintT(self)
            /\ pc' = [pc EXCEPT ![self] = "l2"]
            /\ x' = x

l2(self) == /\ pc[self] = "l2"
            /\ PrintT(x[self])
            /\ pc' = [pc EXCEPT ![self] = "Done"]
            /\ x' = x

p(self) == l1(self) \/ l2(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == (\E self \in 1..3: p(self))
           \/ Terminating

Spec == Init /\ [][Next]_vars

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Sun Mar 28 22:22:46 CEST 2021 by aricci
\* Created Sun Mar 28 19:29:32 CEST 2021 by aricci
