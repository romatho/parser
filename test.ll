target triple = "x86_64-apple-macosx"
%classe.Object = type{ %table.ObjectVTable}
%table.ObjectVTable = type { }
@ObjectVTableGlobal = internal global %table.ObjectVTable { }

%classe.IO = type{ %table.IOVTable}
%table.IOVTable = type { %classe.IO* (%classe.IO*, i8*)*,%classe.IO* (%classe.IO*, i1)*,%classe.IO* (%classe.IO*, i32)*,i8* (%classe.IO*)*,i1 (%classe.IO*)*,i32 (%classe.IO*)*}
@IOVTableGlobal = internal global %table.IOVTable { %classe.IO* (%classe.IO*, i8*)* @IOprint,%classe.IO* (%classe.IO*, i1)* @IOprintBool,%classe.IO* (%classe.IO*, i32)* @IOprintInt32,i8* (%classe.IO*)* @IOinputLine,i1 (%classe.IO*)* @IOinputBool,i32 (%classe.IO*)* @IOinputInt32}

%classe.Main = type{ %table.MainVTable}
%table.MainVTable = type { i32 (%classe.Main*)*}
@MainVTableGlobal = internal global %table.MainVTable { i32 (%classe.Main*)* @Mainmain}

%file = type { i32, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, %marker*, %file*, i32, i32, i64, i16, i8, [1 x i8], i8*, i64, i8*, i8*, i8*, i8*, i64, i32, [20 x i8] }
 %marker = type { %marker*, %file*, i32 }
@true = constant [5 x i8] c"true\00"
@false = constant [6 x i8] c"false\00"
@formatInt = constant [3 x i8] c"%d\00"
@formatString = constant [3 x i8] c"%s\00"
@stdin = external global %file*
@truecmp = constant [6 x i8] c"true\0A\00"
@.strempty = constant [1 x i8] c"\00"

define %classe.IO* @IOprintInt32(%classe.IO* %this, i32 %myInt32){
entry:
%0 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @formatInt , i32 0, i32 0), i32 %myInt32)
	ret %class.IO* %this
}
define %classe.IO* @IOprintBool(%classe.IO* %this, i1 %myBool){
entry:
	%0 = icmp eq i1 %myBool, 0
	br = i1 %1 label %labelFalse, label %labelTrue
labelFalse:
	%1 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @false , i32 0, i32 0))
	ret %class.IO* %thislabelTrue:
%2 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @true , i32 0, i32 0))
	ret %class.IO* %this
}
define %classe.IO* @IOprint(%classe.IO* %this, i8 %myString){
entry:
	%0 = call i32(i8*,...)* @printf(i8 %myString)
	ret classe.IO* %this
}
define i32 @IOinputInt32(%classe.IO* %this, i8 %myString){
entry:
	%0 = alloca %classe.IO*	%n = alloca i32	store %classe.IO* %this, %classe.IO** %1	%1 = load %classe.IO** %1	%2 = call i32 (i8*, ...)* @scanf(i8* getelemntptr inbounds ([3 x i8]* @formatInt, i32 0, i32 0), i32* %n)
	%unused= call i8* @llvmGetLine()	%3= load i32* %n)	ret i32* %4}
define i1 @IOinputBool(%classe.IO* %this){
entry:
	%0 alloc %classe.IO*
	store %classe.IO* %this, %classe.IO** %1
	%1 = load %classe.IO** %0
	%2 = call i8* @llvmGetLine()
	%3 = call i32* strcmp(i8* %2, i8*getelementptr inbound ([6 x i8]* @truecmp, i32 0, i32 0))
	%4 = icmp eq i32 %3, 0
	br i1 %4, label %then, label %else
then:
	br label %end
else:
	br label %end
end:
	%6 = phi i1 [true, %then], [false, %else]
	ret i1 %4}
define i8* @IOinputLine(){
entry:
	%0 = call i8* @llvmGetLine()	ret i8* %0
}
define i* llvmGetLine(){entry:
	%0 = alloca i8*	%line = alloca i8*	%len = alloca i64	%nread = alloca i64	store i8* null, i8** %line	store i64 0, i64* %len	br label %1	%2 = load %struct._IO_FILE** @stdin	%3 = call i64 @getline(i8** %line, i64* %len, %struct._IO_FILE* %2)	store i64 %3, i64* %nread	%5 = icmp ne i64 %3, -1	br i1 %4, label %5, label %6	%7 = load i8** %line	store i8* %7, i8** %0	br label %10	%9 = load i8** %line	store i8* %9, i8** %1	br label %10	%11 = load i8** %1	ret i8* %11}
define i32 @main(){
%1 = call %classe.Main* @Main-new()
%2 = call i32 @Mainmain(%classe.Main* %1)
ret i32 %2
}define i32 @main() {
    %1 = call %class.Main* @Main-new()
    %2 = call i32 @Main-main(%class.Main* %1)
    ret i32 %2
}