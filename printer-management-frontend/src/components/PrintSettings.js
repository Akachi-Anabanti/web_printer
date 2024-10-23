const PrintSettings = ({ settings, onSettingsChange }) => {
  return (
    <div className="print-settings">
      <h3>Print Settings</h3>
      <div>
        <label htmlFor="orientation">Orientation</label>
        <select
          id="orientation"
          value={settings.orientation}
          onChange={(e) => onSettingsChange("orientation", e.target.value)}
        >
          <option value="portrait">Portrait</option>
          <option value="landscape">Landscape</option>
        </select>
      </div>
      <div>
        <label htmlFor="paperSize">Paper Size:</label>
        <select
          id="paperSize"
          value={settings.paperSize}
          onChange={(e) => onSettingsChange("paperSize", e.target.value)}
        >
          <option value="a4">A4</option>
          <option value="letter">Letter</option>
          <option value="Legal">Legal</option>
        </select>
      </div>
      <div>
        <label htmlFor="scale">Scale (%):</label>
        <input
          type="number"
          id="scale"
          min={1}
          max={100}
          value={settings.scale}
          onChange={(e) => onSettingsChange("scale", parseInt(e.target.value))}
        />
      </div>
      <div>
        <label htmlFor="margins">Margins:</label>
        <select
          id="margins"
          value={settings.margins}
          onChange={(e) => onSettingsChange("margins", e.target.value)}
        >
          <option value={"normal"}>Normal</option>
          <option value={"narrow"}>Narrow</option>
          <option value={"wide"}>Wide</option>
        </select>
      </div>
      <div>
        <label htmlFor="backgroundGraphics">
          <input
            type="checkbox"
            id="backgroundGraphics"
            checked={settings.backgroundGraphics}
            onChange={(e) =>
              onSettingsChange("backgroundGraphics", e.target.value)
            }
          />
          Print Background Graphics
        </label>
      </div>
      <div>
        <label htmlFor="pageRange">Pages to print</label>
        <input
          type="text"
          id="pageRange"
          value={settings.pageRange}
          onChange={(e) => onSettingsChange("pageRange", e.target.value)}
          placeholder="e.g 1-5, 8, 11-13"
        />
      </div>
    </div>
  );
};

export default PrintSettings;
