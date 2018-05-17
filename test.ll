target triple amd64-pc-windows
%classe.Object = type{ %table.ObjectVTable}
%table.ObjectVTable = type { }
@ObjectVTableGlobal = type { }

%classe.IO = type{ %table.IOVTable}
%table.IOVTable = type { %classe.IO (*%classe.IO*, i8*)*,%classe.IO (*%classe.IO*, i1)*,%classe.IO (*%classe.IO*, i32)*,%classe.string (*%classe.IO*)*,%classe.bool (*%classe.IO*)*,%classe.int32 (*%classe.IO*)*}
@IOVTableGlobal = type { %classe.IO (*%classe.IO*, i8*)* @IOprint,%classe.IO (*%classe.IO*, i1)* @IOprintBool,%classe.IO (*%classe.IO*, i32)* @IOprintInt32,%classe.string (*%classe.IO*)* @IOinputLine,%classe.bool (*%classe.IO*)* @IOinputBool,%classe.int32 (*%classe.IO*)* @IOinputInt32}

%classe.Main = type{ %table.MainVTable}
%table.MainVTable = type { %classe.int32 (*%classe.Main*)*}
@MainVTableGlobal = type { %classe.int32 (*%classe.Main*)* @Mainmain}

define %classe.IO* @IOprintInt32(%classe.IO* %this, i32 %myInt32){
entry:
%1 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([3 x i8]* @formatInt , i32 0, i32 0), i32 %myInt32)	ret %class.IO* %this
}
define %classe.IO* @IOprintBool(%classe.IO* %this, i1 %myBool){
entry:
	%1 = icmp eq i1 %myBool, 0
	br = i1 %1 label %labelFalse, label %labelTrue 
labelFalse:
%2 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @false , i32 0, i32 0))	ret %class.IO* %thislabelTrue:
%3 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @true , i32 0, i32 0))
	ret %class.IO* %this
}
define %classe.IO* @IOprint(%classe.IO* %this, i8 %myString){
entry:
	%1 = call i32(i8*,...)* @printf(i8 %myString)
	ret classe.IO* %this
}
define i32 @IOinputInt32(%classe.IO* %this, i8 %myString){
entry:
	%1 = alloca %classe.IO*	%n = alloca i32	store %classe.IO* %this, %classe.IO** %1	%2 = load %classe.IO** %1	%3 = call i32 (i8*, ...)* @scanf(i8* getelemntptr inbounds ([3 x i8]* @formatInt, i32 0, i32 0), i32* %n)
	%unused= call i8* @llvmGetLine()	%4= load i32* %n)	ret i32* %4}
define i1 @IOinputBool(%classe.IO* %this){
entry:
	%1 alloc %classe.IO*
	store %classe.IO* %this, %classe.IO** %1
	%2 = load %classe.IO** %1
	%3 = call i8* @llvmGetLine()
	%4 = call i32* strcmp(i8* %3, i8*getelementptr inbound ([6 x i8]* @truecmp, i32 0, i32 0))
	%5 = icmp eq i32 %4, 0
	br i1 %5, label %then, label %else
then:
	br label %end
else:
	br label %end
end:
	%6 = phi i1 [true, %then], [false, %else]
	ret i1 %5}
define i8* @IOinputLine(){
entry:
	%1 = call i8* @llvmGetLine()	ret i8* %1
}
define i* llvmGetLine(){entry:
	%1 = alloca i8*	%line = alloca i8*	%len = alloca i64	%nread = alloca i64	store i8* null, i8** %line	store i64 0, i64* %len	br label %2	%3 = load %struct._IO_FILE** @stdin	%4 = call i64 @getline(i8** %line, i64* %len, %struct._IO_FILE* %3)	store i64 %4, i64* %nread	%5 = icmp ne i64 %4, -1	br i1 %5, label %6, label %	%7 = load i8** %line	store i8* %7, i8** %1	br label %10	%9 = load i8** %line	store i8* %9, i8** %1	br label %10	%11 = load i8** %1	ret i8* %11}
define i32 @main(){
%1 = call %classe.Main* @Main-new()
%2 = call i32 @Mainmain(%classe.Main* %1)
ret i32 %2 
}define i32 @main() {
    %1 = call %class.Main* @Main-new()
    %2 = call i32 @Main-main(%class.Main* %1)
    ret i32 %2
}
