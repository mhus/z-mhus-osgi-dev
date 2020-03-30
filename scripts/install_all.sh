
function inst {
 echo "============================"
 echo $1
 echo "============================"
 if [ ! -d $1 ]; then
   echo Not exists
   return
 fi
 lastUpdate=$(stat -f "%m" $1)
 cd $1
 lastEntry=$(find . -type f -not -path  "*/target/*" -not -path "*/.*"  -exec stat -f "%m %N" \{\} + | sort -r | head -n 1)
 lastFile=$(echo $lastEntry|cut -d ' ' -f 2)
 lastModify=$(echo $lastEntry|cut -d ' ' -f 1)
 if [ $lastUpdate = $lastModify ]; then
   echo Not modified
   cd ..
   return
 fi
 echo Modified $lastFile
 echo mvn $opt
 mvn $opt || exit 1
 cd ..

  lastDate=$(date -r $lastModify +"%Y%m%d%H%M.%S")
  touch -t $lastDate $1
}

if [ -z "$@" ]; then
  opt="install"
else
  opt="$@"
fi

inst mhus-parent
inst mhus-lib

inst mhus-osgi-tools
inst mhus-osgi-crypt
inst mhus-osgi-servlets
inst mhus-transform
inst mhus-vaadin
inst mhus-mongo
inst mhus-rest
inst mhus-micro
inst mhus-dev

inst cherry-reactive
inst cherry-vault
inst cherry-web

