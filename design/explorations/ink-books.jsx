/* DZ Ink & Paper — book-layer screens.
   Exposes window.DZInkBooks = { BookReviews, AuthorDetail, CategoryDetail } */
(function () {
  const { Ic, pal, cardStyle, Frame, Chip, Label, IconBtn, TopBar, Btn, SectionTitle, BookRow, Stars, AV, BOOKS } = window.DZInk;

  /* ---------- Book reviews ---------- */
  function BookReviews({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const bars = [72, 18, 6, 3, 1];
    const reviews = [
      { av: 'profile_2.png', n: 'Patricia Lane', d: '2 days ago', s: 5, txt: 'Atmospheric and slow in the best way. I read the last hundred pages in one sitting with the lights low.' },
      { av: 'img_neil_alvin.png', n: 'Neil Alvin', d: '1 week ago', s: 4, txt: 'Gorgeous prose. The middle drags slightly, but the house itself is the best character I\u2019ve met all year.' },
    ];
    return (
      <Frame t={t} c={c} h={860}>
        <div style={{ height: '100%', position: 'relative', paddingBottom: 84 }}>
          <TopBar t={t} c={c} title="Reviews" sub="Mexican Gothic · Silvia Moreno-Garcia" right={<IconBtn t={t} c={c} n="share" s={16} />} />

          {/* summary */}
          <div style={{ margin: '8px 22px 0', ...cardStyle(t, c), padding: 18, display: 'flex', gap: 20, alignItems: 'center' }}>
            <div style={{ textAlign: 'center' }}>
              <div style={{ font: `${t.displayWeight} 42px/1 ${t.fontDisplay}`, color: c.ink }}>4.6</div>
              <div style={{ marginTop: 7 }}><Stars c={c} n={5} s={11} /></div>
              <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 7 }}>1,284 ratings</div>
            </div>
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 6 }}>
              {bars.map((w, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <span style={{ font: `600 10px/1 ${t.fontBody}`, color: c.muted, width: 8 }}>{5 - i}</span>
                  <div style={{ flex: 1, height: 4, borderRadius: 999, background: c.alt, overflow: 'hidden' }}><div style={{ width: w + '%', height: '100%', background: c.accent, borderRadius: 999 }} /></div>
                </div>
              ))}
            </div>
          </div>

          {/* filters */}
          <div style={{ display: 'flex', gap: 8, padding: '18px 22px 0' }}>
            <Chip t={t} c={c} solid>Most helpful</Chip><Chip t={t} c={c}>Recent</Chip><Chip t={t} c={c}>Critical</Chip>
          </div>

          {/* reviews */}
          <div style={{ padding: '8px 22px 0' }}>
            {reviews.map((r, i) => (
              <div key={i} style={{ padding: '16px 0', borderBottom: i < reviews.length - 1 ? `1px solid ${c.line}` : 'none' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 11 }}>
                  <img src={AV + r.av} alt="" style={{ width: 36, height: 36, borderRadius: t.radiusSm, objectFit: 'cover' }} />
                  <div style={{ flex: 1 }}>
                    <div style={{ font: `700 13px/1 ${t.fontBody}`, color: c.ink }}>{r.n}</div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 7, marginTop: 5 }}>
                      <Stars c={c} n={r.s} s={10} />
                      <span style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted }}>{r.d}</span>
                    </div>
                  </div>
                  <Ic n="three_vertical_dots" s={15} color={c.muted} />
                </div>
                <p style={{ font: `400 13.5px/1.7 ${t.fontBody}`, color: c.inkSoft, margin: '11px 0 0' }}>{r.txt}</p>
                <div style={{ display: 'flex', gap: 16, marginTop: 11 }}>
                  <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, font: `600 11.5px/1 ${t.fontBody}`, color: c.muted }}><Ic n="favorite" s={13} color={i === 0 ? c.accent : c.muted} />{i === 0 ? '32' : '11'} helpful</span>
                  <span style={{ font: `600 11.5px/1 ${t.fontBody}`, color: c.muted }}>Reply</span>
                </div>
              </div>
            ))}
          </div>

          {/* write bar */}
          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, padding: '14px 22px 18px', background: c.paper, borderTop: `1px solid ${c.line}` }}>
            <Btn t={t} c={c}><Ic n="plus" s={14} color={c.onAccent} />Write a review</Btn>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Author detail ---------- */
  function AuthorDetail({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 22px 0' }}>
            <IconBtn t={t} c={c} n="back" />
            <IconBtn t={t} c={c} n="share" s={16} />
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '14px 26px 0' }}>
            <img src={AV + 'author_detail_patricia_avatar.png'} alt="" style={{ width: 96, height: 96, borderRadius: t.radiusSm + 6, objectFit: 'cover', boxShadow: '0 14px 28px rgba(20,18,12,0.22)' }} />
            <div style={{ font: `${t.displayWeight} 26px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 18 }}>Jacqueline Woodson</div>
            <div style={{ font: `400 12.5px/1 ${t.fontAccent}`, fontStyle: 'italic', color: c.muted, marginTop: 8 }}>Novelist · National Book Award winner</div>
          </div>

          {/* stats */}
          <div style={{ display: 'flex', margin: '22px 22px 0', ...cardStyle(t, c), padding: '15px 0' }}>
            {[['Books', '34'], ['Readers', '212k'], ['Avg rating', '4.5']].map((m, i) => (
              <div key={i} style={{ flex: 1, textAlign: 'center', borderLeft: i ? `1px solid ${c.line}` : 'none' }}>
                <div style={{ font: `${t.displayWeight} 17px/1.1 ${t.fontDisplay}`, color: c.ink }}>{m[1]}</div>
                <div style={{ font: `500 10px/1 ${t.fontBody}`, letterSpacing: '0.08em', textTransform: 'uppercase', color: c.muted, marginTop: 5 }}>{m[0]}</div>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', gap: 10, padding: '16px 22px 0' }}>
            <Btn t={t} c={c} h={46} style={{ flex: 1 }}>Follow</Btn>
            <Btn t={t} c={c} h={46} kind="secondary" style={{ width: 46, padding: 0 }}><Ic n="message" s={18} color={c.ink} /></Btn>
          </div>

          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>About</Label>
            <p style={{ font: `400 13.5px/1.7 ${t.fontBody}`, color: c.inkSoft, margin: '11px 0 0', display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
              Jacqueline Woodson writes spare, luminous novels about family, memory and belonging. Her work moves between generations with a poet's ear for what's left unsaid.
            </p>
          </div>

          <div style={{ padding: '22px 0 0' }}>
            <SectionTitle t={t} c={c} action="See all" style={{ padding: '0 22px', marginBottom: 14 }}>By this author</SectionTitle>
            <div style={{ display: 'flex', gap: 16, padding: '0 22px', overflow: 'hidden' }}>
              {[BOOKS[6], BOOKS[5], BOOKS[7]].map((b, i) => (
                <div key={i} style={{ width: 104, flexShrink: 0 }}>
                  <img src={b.c} alt="" style={{ width: 104, height: 152, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 8px 18px rgba(20,18,12,0.16)' }} />
                  <div style={{ font: `${t.displayWeight} 13px/1.25 ${t.fontDisplay}`, color: c.ink, marginTop: 9, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{b.t}</div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 5, marginTop: 5 }}>
                    <Ic n="star" s={11} color={c.accent} /><span style={{ font: `600 11px/1 ${t.fontBody}`, color: c.inkSoft }}>{b.rating}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Category detail ---------- */
  function CategoryDetail({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 22px 0' }}>
            <IconBtn t={t} c={c} n="back" />
            <IconBtn t={t} c={c} n="search" s={17} />
          </div>

          <div style={{ padding: '20px 22px 0' }}>
            <span style={{ font: `400 12px/1 ${t.fontAccent}`, fontStyle: 'italic', color: c.accent }}>Category 01</span>
            <div style={{ font: `${t.displayWeight} 30px/1.1 ${t.fontDisplay}`, color: c.ink, marginTop: 10 }}>Literary fiction</div>
            <p style={{ font: `400 13px/1.6 ${t.fontBody}`, color: c.muted, margin: '10px 0 0' }}>412 books · quiet, character-first novels that stay with you.</p>
          </div>

          <div style={{ display: 'flex', gap: 8, padding: '18px 22px 0', flexWrap: 'wrap' }}>
            <Chip t={t} c={c} solid>All</Chip><Chip t={t} c={c}>New</Chip><Chip t={t} c={c}>Award winners</Chip><Chip t={t} c={c}>Under $10</Chip>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '20px 22px 0' }}>
            <Label t={t} c={c}>Sorted by · most loved</Label>
            <Ic n="arrow_drop_down" s={14} color={c.muted} />
          </div>

          <div style={{ padding: '4px 22px 0' }}>
            {[BOOKS[0], BOOKS[2], BOOKS[4], BOOKS[3]].map((b, i) => (
              <BookRow key={i} t={t} c={c} b={b} top={i > 0}
                meta={<div style={{ display: 'flex', alignItems: 'center', gap: 6 }}><Ic n="star" s={11} color={c.accent} /><span style={{ font: `600 11px/1 ${t.fontBody}`, color: c.inkSoft }}>{b.rating}</span><span style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted }}>· {b.g[0]}</span></div>}
                trailing={
                  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 8 }}>
                    <span style={{ font: `${t.displayWeight} 14px/1 ${t.fontDisplay}`, color: c.ink }}>${b.price}</span>
                    <Ic n="bookmark" s={15} color={i === 1 ? c.accent : c.muted} />
                  </div>
                } />
            ))}
          </div>
        </div>
      </Frame>
    );
  }

  window.DZInkBooks = { BookReviews, AuthorDetail, CategoryDetail };
})();
