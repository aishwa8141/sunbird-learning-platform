
#!/bin/sh
#****************************************************
#AUTHOR:MANOJ V V
#DESCRIPTION:Script for lp-definition-update.
#DATE:25-05-16
#****************************************************
cd /home/jenkins/workspace/LP_Definition_Update/docs/domain_model_v2/definitions                    #move inside th folder

#*****************************************************
#Find all files required & copy the content of the file to a variable .user variable in the curl command to definition-update
for file in *                        
do
   variable=`cat $file`
   curl -i -X POST -H "Content-Type: application/json" -H "user-id: ansible_user" -d "$variable" https://{{ service_url }}/learning/taxonomy/domain/definition
done


