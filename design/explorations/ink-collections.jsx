/* DZ Ink & Paper — collections & goals screens.
   Exposes window.DZInkCollections = { Collections, CollectionDetail, CollectionsEdit, Goal } */
(function () {
  const { Ic, pal, cardStyle, Frame, Nav, Label, IconBtn, TopBar, Btn, Field, BookRow, ToggleSwitch, BOOKS } = window.DZInk;

  const COLLS = [
    { n: 'Quiet novels', count: 12, bs: [BOOKS[4], BOOKS[2], BOOKS[0]] },
    { n: 'For the train', count: 5, bs: [BOOKS[1], BOOKS[3]] },
    { n: 'Lent to friends', count: 3, bs: [BOOKS[2]] },
  ];

  /* ---------- Collections ---------- */
  function Collections({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%', paddingBottom: 64 }}>
          <TopBar t={t} c={c} title="Collections" sub="20 books across 3 shelves" right={<IconBtn t={t} c={c} n="search" s={17} />} />

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, padding: '8px 22px 0' }}>
            {COLLS.map((col, i) => (
              <div key={i} style={{ ...cardStyle(t, c), padding: 13 }}>
                <div style={{ display: 'flex' }}>
                  {col.bs.map((b, j) => (
                    <img key={j} src={b.c} alt="" style={{ width: 44, height: 64, borderRadius: t.cover - 1, objectFit: 'cover', marginLeft: j ? -16 : 0, border: `2px solid ${c.surface}`, boxShadow: '0 4px 10px rgba(20,18,12,0.18)', position: 'relative', zIndex: 3 - j }} />
                  ))}
                </div>
                <div style={{ font: `${t.displayWeight} 14.5px/1.25 ${t.fontDisplay}`, color: c.ink, marginTop: 12 }}>{col.n}</div>
                <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>{col.count} books</div>
              </div>
            ))}
            {/* new collection tile */}
            <div style={{ borderRadius: t.radius, border: `1.5px dashed ${c.line}`, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 9, minHeight: 128 }}>
              <div style={{ width: 34, height: 34, borderRadius: 999, background: c.accentSoft, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Ic n="plus" s={14} color={c.accent} />
              </div>
              <span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>New collection</span>
            </div>
          </div>

          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>Smart shelves</Label>
            <div style={{ marginTop: 6 }}>
              {[['bookmark', 'Saved for later', '8 books'], ['purchased', 'Purchased', '14 books'], ['favorite', 'Loved', '6 books']].map((r, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '13px 0', borderBottom: i < 2 ? `1px solid ${c.line}` : 'none' }}>
                  <div style={{ width: 38, height: 38, borderRadius: t.radiusSm, background: c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n={r[0]} s={16} color={c.accent} /></div>
                  <div style={{ flex: 1 }}>
                    <div style={{ font: `600 13.5px/1 ${t.fontBody}`, color: c.ink }}>{r[1]}</div>
                    <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{r[2]}</div>
                  </div>
                  <span style={{ transform: 'rotate(180deg)', display: 'flex' }}><Ic n="back" s={14} color={c.muted} /></span>
                </div>
              ))}
            </div>
          </div>
        </div>
        <Nav t={t} c={c} active="library" />
      </Frame>
    );
  }

  /* ---------- Collection detail ---------- */
  function CollectionDetail({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 22px 0' }}>
            <IconBtn t={t} c={c} n="back" />
            <div style={{ display: 'flex', gap: 10 }}>
              <IconBtn t={t} c={c} n="share" s={16} />
              <IconBtn t={t} c={c} n="three_vertical_dots" s={16} />
            </div>
          </div>

          <div style={{ padding: '18px 22px 0', display: 'flex', gap: 16, alignItems: 'center' }}>
            <div style={{ display: 'flex' }}>
              {[BOOKS[4], BOOKS[2], BOOKS[0]].map((b, j) => (
                <img key={j} src={b.c} alt="" style={{ width: 56, height: 82, borderRadius: t.cover, objectFit: 'cover', marginLeft: j ? -22 : 0, border: `2px solid ${c.paper}`, boxShadow: '0 8px 16px rgba(20,18,12,0.2)', position: 'relative', zIndex: 3 - j }} />
              ))}
            </div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ font: `${t.displayWeight} 24px/1.1 ${t.fontDisplay}`, color: c.ink }}>Quiet novels</div>
              <div style={{ font: `400 12px/1.4 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>12 books · updated 2 days ago</div>
            </div>
          </div>

          <p style={{ font: `400 13px/1.65 ${t.fontAccent}`, fontStyle: 'italic', color: c.inkSoft, margin: '14px 22px 0' }}>
            "Small lives, carefully observed — the books I reach for on slow evenings."
          </p>

          <div style={{ display: 'flex', gap: 10, padding: '16px 22px 0' }}>
            <Btn t={t} c={c} h={44} kind="soft" style={{ flex: 1 }}><Ic n="plus" s={13} color={c.accent} />Add books</Btn>
            <Btn t={t} c={c} h={44} kind="secondary" style={{ flex: 1 }}>Edit</Btn>
          </div>

          <div style={{ padding: '18px 22px 0' }}>
            {[{ b: BOOKS[4], note: 'read · 5★' }, { b: BOOKS[2], note: 'reading · 62%' }, { b: BOOKS[0], note: 'to read' }, { b: BOOKS[3], note: 'to read' }].map((r, i) => (
              <BookRow key={i} t={t} c={c} b={r.b} top={i > 0}
                meta={<span style={{ font: `400 11px/1 ${t.fontBody}`, color: r.note.includes('reading') ? c.accent : c.muted }}>{r.note}</span>}
                trailing={<Ic n="three_vertical_dots" s={16} color={c.muted} />} />
            ))}
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Edit collection ---------- */
  function CollectionsEdit({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={820}>
        <div style={{ height: '100%', position: 'relative', paddingBottom: 84 }}>
          <TopBar t={t} c={c} title="Edit collection" right={<span style={{ font: `700 13px/1 ${t.fontBody}`, color: c.accent }}>Save</span>} />

          <div style={{ padding: '8px 22px 0' }}>
            <Label t={t} c={c}>Name</Label>
            <div style={{ marginTop: 10 }}><Field t={t} c={c} value="Quiet novels" focus /></div>
          </div>

          <div style={{ padding: '20px 22px 0' }}>
            <Label t={t} c={c}>Description</Label>
            <div style={{ marginTop: 10, borderRadius: t.radiusSm + 2, border: `1px solid ${c.line}`, background: c.surface, padding: '13px 15px', font: `400 13px/1.6 ${t.fontBody}`, color: c.ink, minHeight: 70 }}>
              Small lives, carefully observed — the books I reach for on slow evenings.
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '20px 22px 0' }}>
            <div>
              <div style={{ font: `600 13.5px/1 ${t.fontBody}`, color: c.ink }}>Visible to friends</div>
              <div style={{ font: `400 11.5px/1.4 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>Friends can browse this shelf</div>
            </div>
            <ToggleSwitch c={c} on />
          </div>

          <div style={{ padding: '22px 22px 0' }}>
            <Label t={t} c={c}>Books · drag to reorder</Label>
            <div style={{ marginTop: 4 }}>
              {[BOOKS[4], BOOKS[2], BOOKS[0]].map((b, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '12px 0', borderBottom: i < 2 ? `1px solid ${c.line}` : 'none' }}>
                  <Ic n="move" s={15} color={c.muted} />
                  <img src={b.c} alt="" style={{ width: 38, height: 56, borderRadius: t.cover - 1, objectFit: 'cover' }} />
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ font: `${t.displayWeight} 14px/1.2 ${t.fontDisplay}`, color: c.ink, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{b.t}</div>
                    <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{b.a}</div>
                  </div>
                  <div style={{ width: 30, height: 30, borderRadius: 999, border: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <Ic n="close" s={10} color={c.muted} />
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, padding: '14px 22px 18px', background: c.paper, borderTop: `1px solid ${c.line}`, display: 'flex', justifyContent: 'center' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 8, font: `600 13px/1 ${t.fontBody}`, color: '#B3402A' }}><Ic n="delete" s={15} />Delete collection</span>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Reading goals ---------- */
  function Goal({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const R = 56, CIRC = 2 * Math.PI * R;
    const days = [['M', 1], ['T', 1], ['W', 1], ['T', 1], ['F', 0.6], ['S', 0], ['S', 0]];
    return (
      <Frame t={t} c={c} h={860}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Reading goals" right={<IconBtn t={t} c={c} n="settings" s={17} />} />

          {/* today ring */}
          <div style={{ margin: '8px 22px 0', ...cardStyle(t, c), padding: '24px 18px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <div style={{ position: 'relative', width: 140, height: 140 }}>
              <svg width="140" height="140" viewBox="0 0 140 140">
                <circle cx="70" cy="70" r={R} fill="none" stroke={c.alt} strokeWidth="9" />
                <circle cx="70" cy="70" r={R} fill="none" stroke={c.accent} strokeWidth="9" strokeLinecap="round"
                  strokeDasharray={CIRC} strokeDashoffset={CIRC * (1 - 26 / 30)} transform="rotate(-90 70 70)" />
              </svg>
              <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <div style={{ font: `${t.displayWeight} 30px/1 ${t.fontDisplay}`, color: c.ink }}>26</div>
                <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>of 30 min</div>
              </div>
            </div>
            <div style={{ font: `400 13px/1.5 ${t.fontAccent}`, fontStyle: 'italic', color: c.inkSoft, marginTop: 14 }}>4 minutes to keep the streak alive</div>
          </div>

          {/* week */}
          <div style={{ margin: '14px 22px 0', ...cardStyle(t, c), padding: '16px 18px' }}>
            <Label t={t} c={c}>This week</Label>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 14 }}>
              {days.map(([d, v], i) => (
                <div key={i} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 7 }}>
                  <div style={{ width: 26, height: 26, borderRadius: 999, background: v === 1 ? c.accent : v > 0 ? c.accentSoft : c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    {v === 1 && <Ic n="done" s={10} color={c.onAccent} />}
                    {v > 0 && v < 1 && <span style={{ width: 7, height: 7, borderRadius: 999, background: c.accent }} />}
                  </div>
                  <span style={{ font: `600 10px/1 ${t.fontBody}`, color: i === 4 ? c.ink : c.muted }}>{d}</span>
                </div>
              ))}
            </div>
          </div>

          {/* stats */}
          <div style={{ display: 'flex', gap: 12, margin: '14px 22px 0' }}>
            {[['Streak', '21 days', 'longest 34'], ['This year', '18 books', 'goal 24']].map((s, i) => (
              <div key={i} style={{ flex: 1, ...cardStyle(t, c), padding: '15px 16px' }}>
                <Label t={t} c={c}>{s[0]}</Label>
                <div style={{ font: `${t.displayWeight} 21px/1 ${t.fontDisplay}`, color: c.ink, marginTop: 10 }}>{s[1]}</div>
                <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>{s[2]}</div>
              </div>
            ))}
          </div>

          {/* yearly progress */}
          <div style={{ margin: '14px 22px 0', ...cardStyle(t, c), padding: '16px 18px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline' }}>
              <Label t={t} c={c}>2026 reading goal</Label>
              <span style={{ font: `600 11.5px/1 ${t.fontBody}`, color: c.accent }}>Edit goal</span>
            </div>
            <div style={{ height: 5, borderRadius: 999, background: c.alt, overflow: 'hidden', marginTop: 14 }}><div style={{ width: '75%', height: '100%', background: c.accent, borderRadius: 999 }} /></div>
            <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 9 }}><b style={{ color: c.ink, fontWeight: 700 }}>18 of 24</b> books · 3 ahead of schedule</div>
          </div>
        </div>
      </Frame>
    );
  }

  window.DZInkCollections = { Collections, CollectionDetail, CollectionsEdit, Goal };
})();
