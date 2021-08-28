#!/bin/bash

echo "Download dblp.xml.gz? [Y/n]"
read -r d_flag
case $d_flag in
    [yY][eE][sS]|[yY])
		echo "Yes"
        if [ ! -d "whole" ]; then
                mkdir whole
        fi
        cd whole || exit 1
        curl -O https://dblp.uni-trier.de/xml/dblp.xml.gz.md5
        curl -O https://dblp.uni-trier.de/xml/dblp.xml.gz
        md5sum -c dblp.xml.gz.md5
        gunzip -k dblp.xml.gz
        cd ..
		;;

    [nN][oO]|[nN])
		echo "No"
    ;;

    *)
	echo "Invalid input..."
	exit 1
	;;
esac
if [ -d "./whole/dblp_cvt/" ];then
        rm -rf "./whole/dblp_cvt"
fi

sbt "runMain script.ConvertXml"
sbt "runMain script.Import2MongoDB"
sbt "runMain script.TitleWordCount"
