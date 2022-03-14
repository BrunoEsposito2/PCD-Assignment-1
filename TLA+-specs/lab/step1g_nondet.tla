--------------------------- MODULE step1g_nondet ---------------------------

EXTENDS TLC, Integers, Sequences

(*--algorithm test
    
variables
    c = 0;
        
process p = "p"
begin
l1: 
    either
        c := 1;
    or
        skip;
    end either;
l2: print c;
end process;

process q = "q"
begin
l1:
    with v \in {"a", "b", "c"} do
        print v;
    end with;
end process;


end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "85c2f34a" /\ chksum(tla) = "f6874c2")
\* Label l1 of process p at line 13 col 5 changed to l1_
VARIABLES c, pc

vars == << c, pc >>

ProcSet == {"p"} \cup {"q"}

Init == (* Global variables *)
        /\ c = 0
        /\ pc = [self \in ProcSet |-> CASE self = "p" -> "l1_"
                                        [] self = "q" -> "l1"]

l1_ == /\ pc["p"] = "l1_"
       /\ \/ /\ c' = 1
          \/ /\ TRUE
             /\ c' = c
       /\ pc' = [pc EXCEPT !["p"] = "l2"]

l2 == /\ pc["p"] = "l2"
      /\ PrintT(c)
      /\ pc' = [pc EXCEPT !["p"] = "Done"]
      /\ c' = c

p == l1_ \/ l2

l1 == /\ pc["q"] = "l1"
      /\ \E v \in {"a", "b", "c"}:
           PrintT(v)
      /\ pc' = [pc EXCEPT !["q"] = "Done"]
      /\ c' = c

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
\* Last modified Sun Mar 28 22:33:38 CEST 2021 by aricci
\* Created Sun Mar 28 19:29:32 CEST 2021 by aricci


=============================================================================
\* Modification History
\* Created Sun Mar 28 22:23:35 CEST 2021 by aricci
