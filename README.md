# CVSParentTree
Demonstration for an interview question to put sample data into a hierarchical tree. The output tree in this case is XML output.

The request as given:
The given string has pipe delimited nodes that represent family members in a family tree. Each node is a CSV with the values being "parent_id, node_id, node_name". Write a method that takes an input string and return a single result that represents the data as a hierarchy (root, children, siblings, etc).

Sample input: "null,0,grandpa|0,1,son|0,2,daugther|1,3,grandkid|1,4,grandkid|2,5,grandkid|5,6,greatgrandkid"

• Solve it in any language that you prefer
• Display the hierarchical result any way you prefer (as long as the parent/child connections are clear)

Resulting sample output:
null,0,grandpa
0,1,son
0,2,daugther
1,3,grandkid
1,4,grandkid
2,5,grandkid
5,6,greatgrandkid

<DataElem node_id="0" node_name="grandpa">
   <DataElem node_id="1" node_name="son">
      <DataElem node_id="3" node_name="grandkid"/>
      <DataElem node_id="4" node_name="grandkid"/>
   </DataElem>
   <DataElem node_id="2" node_name="daugther">
      <DataElem node_id="5" node_name="grandkid">
         <DataElem node_id="6" node_name="greatgrandkid"/>
      </DataElem>
   </DataElem>
</DataElem>
