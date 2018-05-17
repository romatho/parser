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

declare i32 @printf(i8*, ...)
 declare i32 @scanf(i8*, ...)
define %classe.IO* @IOprintInt32(%classe.IO* %this, i32 %myInt32){
entry:
%0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @formatInt , i32 0, i32 0), i32 %myInt32)
	ret %classe.IO* %this
}
define %classe.IO* @IOprintBool(%classe.IO* %this, i1 %myBool){
entry:
	%0 = icmp eq i1 %myBool, 0
	br i1 %0, label %labelFalse, label %labelTrue
labelFalse:
	%1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @false , i32 0, i32 0))
	ret %classe.IO* %this
labelTrue:
%2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @true , i32 0, i32 0))
	ret %classe.IO* %this
}
define %classe.IO* @IOprint(%classe.IO* %this, i8* %myString){
entry:
	%0 = call i32(i8*,...) @printf(i8* %myString)
	ret %classe.IO* %this
}
define i32 @IOinputInt32(%classe.IO* %this){
entry:
	%0 = alloca %classe.IO*
	%n = alloca i32
	store %classe.IO* %this, %classe.IO** %0
	%1 = load %classe.IO*, %classe.IO** %0
	%2 = call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8],[3 x i8]* @formatInt, i32 0, i32 0), i32* %n)
	%unused= call i8* @llvmGetLine()
	%3= load i32, i32* %n
	ret i32 %3
}
define i1 @IOinputBool(%classe.IO* %this){
entry:
	%0=  alloca %classe.IO*
	store %classe.IO* %this, %classe.IO** %0
	%1 = load %classe.IO*, %classe.IO** %0
	%2 = call i8* @llvmGetLine()
	%3 = call i32 @strcmp(i8* %2, i8*getelementptr inbounds ([6 x i8],[6 x i8]* @truecmp, i32 0, i32 0))
	%4 = icmp eq i32 %3, 0
	br i1 %4, label %then, label %else
then:
	br label %end
else:
	br label %end
end:
	%5 = phi i1 [true, %then], [false, %else]
	ret i1 %4}
define i8* @IOinputLine(%classe.IO* %this){
entry:
	%0 = call i8* @llvmGetLine()	ret i8* %0
}
define i8* @llvmGetLine(){
entry:
	%0 = alloca i8*
	%line = alloca i8*
	%len = alloca i64
	%nread = alloca i64
	store i8* null, i8** %line
	store i64 0, i64* %len
	br label %1
	%2 = load %file*,%file** @stdin
	%3 = call i64 @getline(i8** %line, i64* %len, %file* %2)
	store i64 %3, i64* %nread
	%4 = icmp ne i64 %3, -1
	br i1 %4, label %5, label %7
	%6 = load i8*,i8** %line
	store i8* %6, i8** %0
	br label %9
	%8 = load i8*,i8** %line
	store i8* %8, i8** %0
	br label %9
	%10 = load i8*, i8** %0
	ret i8* %10
}

define i32 @main(){
%1 = call %classe.Main* @Main-new()
%2 = call i32 @Mainmain(%classe.Main* %1)
ret i32 %2
}define %class.Main* @Main-new() {
    %size = getelementptr %class.Main* null, i32 1
    %sizeI = ptrtoint %class.Main* %size to i64
    %1 = call noalias i8* @malloc(i64 %sizeI)
    %self = bitcast i8* %1 to %class.Main*
    call void @Main-new-init(%class.Main* %self)
    ret %class.Main* %self
}

define void @Main-new-init(%class.Main* %this) {
    %1 = bitcast %class.Main* %this to %class.IO*
    call void @IO-new-init(%class.IO* %1)
    %2 = getelementptr inbounds %class.Main* %this, i32 0, i32 0
    store %struct.Main_vtable* @Main_vtable_inst, %struct.Main_vtable** %2
    ret void
}

define i32 @Mainmain(%classe.Main* %this, ){
entry:
    %0 = alloca %classe.Main* ;%this
    store %classe.Main* %this, %classe.Main** %0
    %1 = load %classe.Main** %0
    %2 = getelementptr inbounds %class.Main* %1, i32 0, i32 0
    %3 = load %struct.Main_vtable** %2
    %4 = getelementptr inbounds %struct.Main_vtable* %3, i32 0, i32 2
    %5 = load %classe.IO* {}** %4
    %6 = call %classe.IO* %5 (%classe.Main* %1, null true)
    %7 = load %classe.Main** %0
    %8 = getelementptr inbounds %class.Main* %7, i32 0, i32 0
    %9 = load %struct.Main_vtable** %8
    %10 = getelementptr inbounds %struct.Main_vtable* %9, i32 0, i32 2
    %11 = load %classe.IO* {}** %10
    %12 = call %classe.IO* %11 (%classe.Main* %7, null getelementptr inbounds ([2 x i8]* @.str, i32 0, i32 0))
    ret i32
}
null