------------------------------ MODULE step3_cs ------------------------------

(*--algorithm critical_section
  
define
    MutualExclusion == []~(pc["p1"] = "CS" /\ pc["p2"] = "CS")
end define;
    
process proc \in {"p1", "p2"}
begin MainLoop:
  while TRUE do
    NCS: skip;
    CS: skip;
  end while;
end process;


end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "25e1bb66" /\ chksum(tla) = "9befd3ea")
VARIABLE pc

(* define statement *)
MutualExclusion == []~(pc["p1"] = "CS" /\ pc["p2"] = "CS")


vars == << pc >>

ProcSet == ({"p1", "p2"})

Init == /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ pc' = [pc EXCEPT ![self] = "NCS"]

NCS(self) == /\ pc[self] = "NCS"
             /\ TRUE
             /\ pc' = [pc EXCEPT ![self] = "CS"]

CS(self) == /\ pc[self] = "CS"
            /\ TRUE
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]

proc(self) == MainLoop(self) \/ NCS(self) \/ CS(self)

Next == (\E self \in {"p1", "p2"}: proc(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 


=============================================================================
\* Modification History
\* Last modified Sun Mar 28 15:12:41 CEST 2021 by aricci
\* Created Sun Mar 28 15:09:30 CEST 2021 by aricci
