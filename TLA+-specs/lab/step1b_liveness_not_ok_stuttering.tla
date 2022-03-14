----------------- MODULE step1b_liveness_not_ok_stuttering -----------------

EXTENDS TLC, Integers

(*--algorithm step0  
variables 
    c = 0;

define 
    NoOverflowInvariant == (c < 3) 
    ProperFinalValue == <>(c = 1 \/ c = 2)
end define;

process p = "p"
variables a = 1
begin
l1: c := a;
end process;

process q = "q"
variables b = 2
begin
l2: c := b;
end process;

end algorithm;*) 

\* BEGIN TRANSLATION (chksum(pcal) = "54019dfa" /\ chksum(tla) = "f3fa464a")
VARIABLES c, pc

(* define statement *)
NoOverflowInvariant == (c < 3)
ProperFinalValue == <>(c = 1 \/ c = 2)

VARIABLES a, b

vars == << c, pc, a, b >>

ProcSet == {"p"} \cup {"q"}

Init == (* Global variables *)
        /\ c = 0
        (* Process p *)
        /\ a = 1
        (* Process q *)
        /\ b = 2
        /\ pc = [self \in ProcSet |-> CASE self = "p" -> "l1"
                                        [] self = "q" -> "l2"]

l1 == /\ pc["p"] = "l1"
      /\ c' = a
      /\ pc' = [pc EXCEPT !["p"] = "Done"]
      /\ UNCHANGED << a, b >>

p == l1

l2 == /\ pc["q"] = "l2"
      /\ c' = b
      /\ pc' = [pc EXCEPT !["q"] = "Done"]
      /\ UNCHANGED << a, b >>

q == l2

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
\* Last modified Sun Mar 28 19:16:42 CEST 2021 by aricci
\* Created Sun Mar 28 06:28:17 CEST 2021 by aricci


=============================================================================
\* Modification History
\* Created Sun Mar 28 19:16:03 CEST 2021 by aricci
