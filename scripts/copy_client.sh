#!/bin/bash
if [ -f web-app/jbrowse/index.html ]; then 
	rm -rf web-app/jbrowse/
    cp -r jbrowse-download web-app/jbrowse/
	cp -r client/apollo web-app/jbrowse/plugins/WebApollo
	cp -r client/llsa web-app/jbrowse/plugins/LLSA
    echo "Web Apollo client installed" ; 
else
    echo "ERROR!!!!: JBrowse not installed, can not install client." ; 
fi


