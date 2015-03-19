function commandRoboFlow(task,varargin)
persistent t
if isempty(t) || ~strcmp(t.Status,'open')
    try
        t = tcpip('localhost', 8000, 'NetworkRole', 'client');
        fopen(t);
    catch
        disp('Cannot connect to RoboFlow Control');
        clear t
        return;
    end
end
cmd = '{"@type":"main.model.tasks.basictasks.';
switch task
    case 1 % MoveToWellTask
        cmd = [cmd,'MoveToWellTask","plate":"Plate1","row":"',varargin{1}, ...
            '","column":"',varargin{2},'"}'];
    case 2 % MoveToLocTask
        cmd = [cmd,'MoveToLocTask","xDestination":"',varargin{1}, ...
            '","yDestination":"',varargin{2},'"}'];
    case 3 % LowerTask
        cmd = [cmd,'LowerTask"}'];
    case 4 % RaiseTask
        cmd = [cmd,'RaiseTask"}'];
    case 5 % DispenseTask
        cmd = [cmd,'DispenseTask","volume":"',varargin{1},'"}'];
    case 6 % DelayTask
        cmd = [cmd,'DelayTask","time":"',varargin{1},'"}'];
    case 7 % MultiTask
        
    case 8 % 
        
    case 'c'
        fclose(t);
        delete(t);
        clear t
        return
    otherwise
        return
end
encCMD = unicode2native(cmd,'UTF-8');
fwrite(t,[typecast(swapbytes(uint16(numel(encCMD))),'uint8'),encCMD],'uint8');