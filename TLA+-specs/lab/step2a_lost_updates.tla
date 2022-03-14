------------------------ MODULE step2a_lost_updates ------------------------

EXTENDS TLC, Integers

(*--algorithm lost_updates  
variables 
    c = 0;

define 
    ProperFinalValue == <>(c = 2)
end define;

fair process p = "p"
variables temp = 0;
begin
    l1: temp := c;
    l2: c := temp + 1; 
end process;

fair process q = "q"
variables temp = 0;
begin  
    l1: temp := c;
    l2: c := temp + 1;
end process;

end algorithm;*) 
\* BEGIN TRANSLATION (chksum(pcal) = "686e98ca" /\ chksum(tla) = "2444f4bf")
\* Label l1 of process p at line 16 col 9 changed to l1_
\* Label l2 of process p at line 17 col 9 changed to l2_
\* Process variable temp of process p at line 14 col 11 changed to temp_
VARIABLES c, pc

(* define statement *)
ProperFinalValue == <>(c = 2)

VARIABLES temp_, temp

vars == << c, pc, temp_, temp >>

ProcSet == {"p"} \cup {"q"}

Init == (* Global variables *)
        /\ c = 0
        (* Process p *)
        /\ temp_ = 0
        (* Process q *)
        /\ temp = 0
        /\ pc = [self \in ProcSet |-> CASE self = "p" -> "l1_"
                                        [] self = "q" -> "l1"]

l1_ == /\ pc["p"] = "l1_"
       /\ temp_' = c
       /\ pc' = [pc EXCEPT !["p"] = "l2_"]
       /\ UNCHANGED << c, temp >>

l2_ == /\ pc["p"] = "l2_"
       /\ c' = temp_ + 1
       /\ pc' = [pc EXCEPT !["p"] = "Done"]
       /\ UNCHANGED << temp_, temp >>

p == l1_ \/ l2_

l1 == /\ pc["q"] = "l1"
      /\ temp' = c
      /\ pc' = [pc EXCEPT !["q"] = "l2"]
      /\ UNCHANGED << c, temp_ >>

l2 == /\ pc["q"] = "l2"
      /\ c' = temp + 1
      /\ pc' = [pc EXCEPT !["q"] = "Done"]
      /\ UNCHANGED << temp_, temp >>

q == l1 \/ l2

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == p \/ q
           \/ Terminating

Spec == /\ Init /\ [][Next]_vars
        /\ WF_vars(p)
        /\ WF_vars(q)

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 


=============================================================================
\* Modification History
\* Last modified Sun Mar 28 14:44:04 CEST 2021 by aricci
\* Created Sun Mar 28 14:41:33 CEST 2021 by aricci
