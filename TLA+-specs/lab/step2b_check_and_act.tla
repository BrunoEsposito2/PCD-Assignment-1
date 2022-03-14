------------------------ MODULE step2b_check_and_act ------------------------

EXTENDS TLC, Integers

(*--algorithm check_act 
variables 
    c = 0;

define 
    InRangeInvariant == (c >= 0) /\ (c <= 1) 
end define;

fair process p \in {"p1", "p2"} 
begin
l1: while TRUE do
l2:   if c < 1 then 
l3:      c := c + 1;
      end if;
    end while;
end process;

fair process q = "q"
variables temp = 0;
begin
l1: while TRUE do
l2:   if c > 0 then 
l3:      c := c - 1;
    end if;
  end while;
end process;

end algorithm;*) 
\* BEGIN TRANSLATION (chksum(pcal) = "5c728d9e" /\ chksum(tla) = "e32e02ca")
\* Label l1 of process p at line 15 col 5 changed to l1_
\* Label l2 of process p at line 16 col 7 changed to l2_
\* Label l3 of process p at line 17 col 10 changed to l3_
VARIABLES c, pc

(* define statement *)
InRangeInvariant == (c >= 0) /\ (c <= 1)

VARIABLE temp

vars == << c, pc, temp >>

ProcSet == ({"p1", "p2"}) \cup {"q"}

Init == (* Global variables *)
        /\ c = 0
        (* Process q *)
        /\ temp = 0
        /\ pc = [self \in ProcSet |-> CASE self \in {"p1", "p2"} -> "l1_"
                                        [] self = "q" -> "l1"]

l1_(self) == /\ pc[self] = "l1_"
             /\ pc' = [pc EXCEPT ![self] = "l2_"]
             /\ UNCHANGED << c, temp >>

l2_(self) == /\ pc[self] = "l2_"
             /\ IF c < 1
                   THEN /\ pc' = [pc EXCEPT ![self] = "l3_"]
                   ELSE /\ pc' = [pc EXCEPT ![self] = "l1_"]
             /\ UNCHANGED << c, temp >>

l3_(self) == /\ pc[self] = "l3_"
             /\ c' = c + 1
             /\ pc' = [pc EXCEPT ![self] = "l1_"]
             /\ temp' = temp

p(self) == l1_(self) \/ l2_(self) \/ l3_(self)

l1 == /\ pc["q"] = "l1"
      /\ pc' = [pc EXCEPT !["q"] = "l2"]
      /\ UNCHANGED << c, temp >>

l2 == /\ pc["q"] = "l2"
      /\ IF c > 0
            THEN /\ pc' = [pc EXCEPT !["q"] = "l3"]
            ELSE /\ pc' = [pc EXCEPT !["q"] = "l1"]
      /\ UNCHANGED << c, temp >>

l3 == /\ pc["q"] = "l3"
      /\ c' = c - 1
      /\ pc' = [pc EXCEPT !["q"] = "l1"]
      /\ temp' = temp

q == l1 \/ l2 \/ l3

Next == q
           \/ (\E self \in {"p1", "p2"}: p(self))

Spec == /\ Init /\ [][Next]_vars
        /\ \A self \in {"p1", "p2"} : WF_vars(p(self))
        /\ WF_vars(q)

\* END TRANSLATION 


=============================================================================
\* Modification History
\* Last modified Sun Mar 28 14:58:57 CEST 2021 by aricci
\* Created Sun Mar 28 14:46:41 CEST 2021 by aricci
