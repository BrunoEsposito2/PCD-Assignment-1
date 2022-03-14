------------------------- MODULE step1a_invariants -------------------------

EXTENDS TLC, Integers

(*--algorithm step1a_invariants  
variables 
    c = 0;

define 
    NoOverflowInvariant == (c < 3) 
end define;

process p = "p"
variables a = 1
begin
l1: c := a;
end process;

process q = "q"
variables b = 2
begin
l1: c := b;
end process;

end algorithm;*) 

\* BEGIN TRANSLATION (chksum(pcal) = "ec1ccd7d" /\ chksum(tla) = "c6322d45")
\* Label l1 of process p at line 16 col 5 changed to l1_
VARIABLES c, pc

(* define statement *)
NoOverflowInvariant == (c < 3)

VARIABLES a, b

vars == << c, pc, a, b >>

ProcSet == {"p"} \cup {"q"}

Init == (* Global variables *)
        /\ c = 0
        (* Process p *)
        /\ a = 1
        (* Process q *)
        /\ b = 2
        /\ pc = [self \in ProcSet |-> CASE self = "p" -> "l1_"
                                        [] self = "q" -> "l1"]

l1_ == /\ pc["p"] = "l1_"
       /\ c' = a
       /\ pc' = [pc EXCEPT !["p"] = "Done"]
       /\ UNCHANGED << a, b >>

p == l1_

l1 == /\ pc["q"] = "l1"
      /\ c' = b
      /\ pc' = [pc EXCEPT !["q"] = "Done"]
      /\ UNCHANGED << a, b >>

q == l1

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == p \/ q
           \/ Terminating

Spec == Init /\ [][Next]_vars

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Sun Mar 28 19:13:30 CEST 2021 by aricci
\* Created Sun Mar 28 06:28:17 CEST 2021 by aricci


=============================================================================
\* Modification History
\* Created Sun Mar 28 19:10:48 CEST 2021 by aricci
